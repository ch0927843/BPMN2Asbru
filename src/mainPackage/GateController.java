package mainPackage;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ANNIEConstants;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.persist.PersistenceException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import Conditions.ConditionBase;

/**
 * the gate controller handels the communication with gate and with metamap, over a plug-in for gate,
 * it instanciates the necessary objects of gate, configures it, annotates input documents, executes them with gate and metamap
 * and holds an instance of the condition-translater, which translates gate-documents into logic conditions
 * 
 * @author Christian Hinterer
 *
 */
public class GateController 
{
	// unnecessary (not used) at current implementation
	public GateController()
	{
		this("C:\\Program Files\\GATE_Developer_7.0", null);
	}
	
	/**
	 * 
	 * @param gateHomeDir home directory of the installed gate
	 * @param metaMapConfiguration container for metamap configuration
	 */
	public GateController(String gateHomeDir, MetaMapConfiguration metaMapConfiguration)
	{
		if (!gateHomeDir.isEmpty())
		{
			System.setProperty("gate.home", gateHomeDir);
		}
		else
		{
			System.setProperty("gate.home", "C:\\Program Files\\GATE_Developer_7.0");
		}
		
		this.metaMapConfiguration = metaMapConfiguration;
		InitGateComponents();
		conditionTranslator = new ConditionTranslator();
		
		conditionExpressions = new ArrayList<ConditionExpression>();
	}
	
	// initializes the gate framework (gate embedded), the gate ANNIE and the metamap plug-in
	// returns a instance of a gate processing-resource -> at the moment for the metamap plug-in
	/**
	 * initializes the gate framework, gate embedded, the gate ANNIE, the metamap plug-in and the number tagger
	 */
	@SuppressWarnings("deprecation")
	public void InitGateComponents()
	{
		try 
		{
			log.log(new LogRecord(Level.INFO, "starting gate initialization"));
			// init gate itself
			Gate.init();
			log.log(new LogRecord(Level.INFO, "gate initialization sucessful"));
			
			log.log(new LogRecord(Level.INFO, "register annie"));
			// register annie controller
			Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURL());
			log.log(new LogRecord(Level.INFO, "annie registration sucessful"));
			InitAnnieController();
			
			log.log(new LogRecord(Level.INFO, "register MetaMap"));
			// register metamap controller
			Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tagger_MetaMap").toURL());
			log.log(new LogRecord(Level.INFO, "MetaMap registration sucessful"));
			InitMetaMapController();
			
			log.log(new LogRecord(Level.INFO, "register tagger_numbers"));
			// register number controller
			Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tagger_Numbers").toURL());
			log.log(new LogRecord(Level.INFO, "tagger_numbers registration sucessful"));
			InitNumberController();
		}
		catch (GateException e) 
		{
			log.log(new LogRecord(Level.WARNING, "gate initialization failed: " + e.toString()));
			System.out.println("Error on initializing gate!");
			//e.printStackTrace();
		} 
		catch (MalformedURLException e) 
		{
			log.log(new LogRecord(Level.WARNING, "registering annie controller failed: " + e.toString()));
			System.out.println("Error on registering annie controller!");
			//e.printStackTrace();
		}
	}
	
	/**
	 * calls the method for finding CoditionExpression-tags in the input file and logs count of findings
	 * 
	 * @param fileName file name of the input file
	 * @return list of conditionExpression that contain the plain-text phrase of the condition-tag and the id
	 */
	public Collection<ConditionExpression> ParseFileForConditionExpressionStatements(String fileName)
	{
		int expressionCount = 0;
		
		log.log(new LogRecord(Level.INFO, "searching condition expressions..."));

		expressionCount = FindConditionExpressionStatements(fileName);
		
		log.log(Level.INFO, expressionCount + " condition expressions found in input file");
		
		return conditionExpressions;
	}
	
	// processes the plain-text condition phrases with gate and metaMap (creates annotations)
	/**
	 * processes the plain-text condition phrases with gate and metaMap. This creates annotations on the condition phrases and
	 * calls the method to translate the annotated condition phrases into logic conditions 
	 * 
	 * @param conditionExpressions the list of plain-text conditions to be processed with gate
	 * @return a list of classes that contain the found logic conditions
	 * @throws ConnectException ConnectException: unable to establish connection
	 */
	public Collection<ConditionBase> ProcessConditionExpressionStatements(Collection<ConditionExpression> conditionExpressions) throws ConnectException
	{
		Collection<ConditionBase> conditions = new ArrayList<ConditionBase>();
		Corpus corpus;
		
		try 
		{
			// creates corpus
			corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");

			// adds documents (condition phrses) to corpus
			for(ConditionExpression con: conditionExpressions)
			{
				Document doc = Factory.newDocument(con.Expression());
				corpus.add(doc);
			}
			
			annieController.setCorpus(corpus);
			annieController.execute();
			
			// calls the method to create conditions out of annotated gate documents
			conditions = CreateConditionsFromAnnotatedStatements();
		}
		catch (ResourceInstantiationException e) 
		{
			log.log(new LogRecord(Level.WARNING, "error on creating gate document or corpus: " + e.toString()));
			System.out.println("Error on creating gate document or corpus");
			//e.printStackTrace();
		}
		catch (ExecutionException e) 
		{
			log.log(new LogRecord(Level.WARNING, "error on executing gate controller: " + e.toString()));
			System.out.println("Error on executing gate controller");
			//e.printStackTrace();
		}
		
		return conditions;
	}
	
	/**
	 * combines the find- and the process-method with the background of offering one single method call for external components
	 * 
	 * @param fileName file name of the input file
	 * @return list of classes that contain the found conditions
	 */
	public Collection<ConditionBase> FindAndProcessConditionExpressionStatements(String fileName)
	{
		try 
		{
			Collection<ConditionExpression> conditionExpressions = ParseFileForConditionExpressionStatements(fileName);
			
			if (conditionExpressions == null || conditionExpressions.isEmpty())
			{
				return new ArrayList<ConditionBase>();
			}
			else
			{
				return ProcessConditionExpressionStatements(conditionExpressions);
			}
			
		} 
		catch (ConnectException e) 
		{
			log.log(new LogRecord(Level.WARNING, "error on connecting to MetaMap Prolog server: " + e.toString()));
			System.out.println("Error on connecting to MetaMap Prolog server");
			return new ArrayList<ConditionBase>();
		}	
	}
	
	/**
	 * takes meta map configuration
	 * 
	 * @param metaMapConfiguration container with meta map configuration informations
	 */
	public void SetMetaMapConfiguration(MetaMapConfiguration metaMapConfiguration)
	{
		this.metaMapConfiguration = metaMapConfiguration;
	}
	
	/**
	 * this class adds additional annotations that are typical for some conditions like some special words, terms and symbols
	 */
	@SuppressWarnings("unchecked")
	private void AddHelperAnnotations(Document doc)
	{ 
		ArrayList<Integer> braceContentStartOffsets = new ArrayList<Integer>();
		ArrayList<Boolean> isLogicBrace = new ArrayList<Boolean>();
		
		conditionTranslator.ResetLogicBraceLayerCount();
		
		try 
		{

			DocumentContent docContent = doc.getContent();
			
			AnnotationSet markups = doc.getAnnotations();
			AnnotationSet tokenMarkups = markups.get("Token");
			
			ArrayList<Annotation> tokenMarkupList = new ArrayList<Annotation>(tokenMarkups);
			Collections.sort(tokenMarkupList);
			
			Iterator<Annotation> tokens = tokenMarkupList.iterator();
			
			// for each token (every phrase sepperated from other phrases by blanks)
			while(tokens.hasNext())
			{
				Annotation token = tokens.next(); 
				FeatureMap features = token.getFeatures();
				long lowerOffset = token.getStartNode().getOffset();
				long upperOffset = token.getEndNode().getOffset();
				
				String type = GetAnnotationType(docContent.getContent(lowerOffset, upperOffset).toString());
				
				// if the word or the sign is a typical for some sort of condition
				if (type != null)
				{
					if (type.equals("openBrace")) // if it is an open brace, the offset is saved to mark the whole brace content on the brace closed sign
					{
						braceContentStartOffsets.add((int)(lowerOffset + 1));
						isLogicBrace.add(isLastTokenALogicOperator); // if the open brace follows to an logic operator it is a "logic brace"
					}
					if (type.equals("closeBrace")) // on the close brace the whole brace content is annotated (including the actual depth of the brace structure)
					{
						int start = braceContentStartOffsets.get(braceContentStartOffsets.size() - 1);
						boolean isLogicOp = isLogicBrace.get(isLogicBrace.size() - 1);
						if (isLogicOp)
						{
							markups.add((long)start, (long)(upperOffset - 1), "logicBraceContentLayer" + braceContentStartOffsets.size(), features);
							conditionTranslator.IncrementLogicBraceLayerCount();
							//markups.add((long)start, (long)(upperOffset - 1), "braceContentLayer", features);
						}
						else
						{
							markups.add((long)start, (long)(upperOffset - 1), "informationBrace", features);
						}
						
						braceContentStartOffsets.remove(braceContentStartOffsets.size() - 1);
						isLogicBrace.remove(isLogicBrace.size() - 1);
					}
					
					// add an annotation
					markups.add(lowerOffset, upperOffset, type, features);
				}
			}
		}
		catch (InvalidOffsetException e)
		{
			log.log(new LogRecord(Level.WARNING, "error on annotating condition phrases: " + e.toString()));
			System.out.println("Error on creating annotations");
			////e.printStackTrace();
		}
			
	}
	
	/**
	 * this method iterates the found and meanwhile annotated condition phrases and uses an instance of the conditionTranslator class 
	 * to translate the annotated gate documents into conditions
	 * 
	 * @return list of conditions
	 */
	private Collection<ConditionBase> CreateConditionsFromAnnotatedStatements()
	{
		Collection<ConditionBase> conditions = new ArrayList<ConditionBase>();
		
		Iterator<Document> documentIterator = annieController.getCorpus().iterator();
		Iterator<ConditionExpression> conditionExpressionIterator = conditionExpressions.iterator();
		
		while(documentIterator.hasNext() && conditionExpressionIterator.hasNext()) // all condition statements are iteratred...
		{
			Document doc = documentIterator.next();
			ConditionExpression conExp = conditionExpressionIterator.next();
			
			AddHelperAnnotations(doc); //...additational annotatinos are created...
			
			conditions.add(CreateSingleCondition(doc, conExp.Id())); //... and the conditions are translated and put into a list
		}
		
		conditionTranslator.PrintStatistics();
		
		return conditions;
	}
	
	/**
	 * splits some special key words, phrases and symbols into annotation groups
	 * 
	 * @param token the word, sign, term, symbol
	 * @return name of the annotation group the token fits in
	 */
	private String GetAnnotationType(String token)
	{
		//String t = token.toLowerCase();
		String t = token;
		
		if (t.equals("AND") || t.equals("OR") || t.equals("XOR"))
		{
			isLastTokenALogicOperator = true;
			
			return "logicOperator";
		}
		if (t.equals("and") || t.equals("or"))
		{
			isLastTokenALogicOperator = false;
			
			return "lingualLogicOperator";
		}
		
		t = token.toLowerCase();
		
		if (t.equals("not") || t.equals("nothing") || t.equals("without") || t.equals("cannot"))
		{
			isLastTokenALogicOperator = false;
			
			return "negation";
		}
		else if (t.equals("with") || t.equals("who") || t.equals("when"))
		{
			isLastTokenALogicOperator = false;
			
			return "reference";
		}
		else if (t.equals("("))
		{
			return "openBrace";
		}
		else if (t.equals(")"))
		{
			isLastTokenALogicOperator = false;
			
			return "closeBrace";
		}
		else if (t.equals(","))
		{
			isLastTokenALogicOperator = false;
			
			return "colon";
		}
		else if (t.equals("inlude") || t.equals("including") || t.equals("includes") || t.equals("with") || t.equals("in"))
		{
			isLastTokenALogicOperator = false;
			
			return "contains";
		}
		else if (t.equals("between") || t.equals("to") || t.equals("from"))
		{
			isLastTokenALogicOperator = false;
			
			return "range";
		}
		else if (t.equals("=")|| t.equals("<") || t.equals(">") || t.equals("!") ||
				 t.equals("equal") || t.equals("equals") || t.equals("greater") || t.equals("less"))
				 //t.equals(">=") || t.equals("==") || t.equals("!=") || t.equals("<>")  t.equals("<=") ||
				 //t.equals("smaller") || t.equals("lower") || t.equals("less") || t.equals("bigger") || 
				 //t.equals("higher") || t.equals("greater") || t.equals("more") || t.equals("than"))
		{
			isLastTokenALogicOperator = false;
			
			return "comparisonOperator";
		}
		
		isLastTokenALogicOperator = false;
		
		return null;
	}
	
	/**
	 * calls the translate method of the conditionTranslator for one single gate document
	 * 
	 * @param doc the annotated gate document
	 * @param id the ID to identify the the condition term
	 * @return the found condition
	 */
	private ConditionBase CreateSingleCondition(Document document, String id)
	{
		ConditionBase condition = null;
		
		condition = conditionTranslator.Translate(document, id);
		
		return condition;
	}
	
	/**
	 * this method selects all plain-text conditions of an input file that are marked
	 * by the xml-tag conditionExpression
	 * 
	 * @param inputFile
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private int FindConditionExpressionStatements(String inputFile)
	{
		int count = 0;
		
		try 
		{
			Document inputDoc = Factory.newDocument(new File(inputFile).toURL()); 
			DocumentContent inputDocContent = inputDoc.getContent();
			
			String annotationSetName = inputDoc.getAnnotationSetNames().toArray()[0].toString();
			AnnotationSet originalMarkups = inputDoc.getAnnotations(annotationSetName);
			// selects the gate annotation for the xml-tag conditionExpression
			AnnotationSet conditionExpressionAnnotations = originalMarkups.get("conditionExpression");
			
			for(Annotation a: conditionExpressionAnnotations.get())
			{
				long begin = a.getStartNode().getOffset();
				long end = a.getEndNode().getOffset();
				
				String condition = inputDocContent.getContent(begin, end).toString();
				
				FeatureMap features = a.getFeatures();
				String id = features.get("id").toString();

				// create a instance of the ConditionExpression class (container for plain-text condition and corresponding id)
				ConditionExpression exp = new ConditionExpression(id, condition);
				conditionExpressions.add(exp);
				
				//System.out.println(id);
				//System.out.println(condition);
				count++;
			}
			
			return count;
		}
		catch (ResourceInstantiationException e) 
		{
			log.log(new LogRecord(Level.WARNING, "error on creating gate corpus: " + e.toString()));
			System.out.println("Error on creating gate corpus");
			////e.printStackTrace();
			return 0;
		} 
		catch (MalformedURLException e) 
		{
			log.log(new LogRecord(Level.WARNING, "error on creating gate corpus: " + e.toString()));
			System.out.println("Error on creating gate corpus");
			////e.printStackTrace();
			return 0;
		} 
		catch (InvalidOffsetException e) 
		{
			log.log(new LogRecord(Level.WARNING, "error on parsing conditions: " + e.toString()));
			System.out.println("Error on parsing conditions");
			////e.printStackTrace();
			return 0;
		}
		catch (NullPointerException e)
		{
			log.log(new LogRecord(Level.WARNING, "input file is not a valid XML: " + e.toString()));
			System.out.println("!!! input file must be a valid XML file !!!");
			////e.printStackTrace();
			return 0;
		}
	}
		
	/**
	 * inits an annie-system, respectively an annie controller -> instance that contains content as document
	 * or as set of documents corpus and processing resources that belong to a defined annie system
	 */
	private void InitAnnieController()
	{
		try
		{
			log.log(new LogRecord(Level.INFO, "starting annie initialization"));

			// load annie controller from file with defaults
		    annieController = (SerialAnalyserController)PersistenceManager.loadObjectFromFile
					(new File(new File(Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR), ANNIEConstants.DEFAULT_FILE));

/*			String[] prNames = ANNIEConstants.PR_NAMES;
			for (int i = 0; i < prNames.length; i++) 
			{
				String element = prNames[i];
				FeatureMap params = Factory.newFeatureMap(); // use default parameters
				ProcessingResource pr = (ProcessingResource)Factory.createResource(element, params);
				
				// add the PR to the pipeline controller
				annieController.add(pr);
			}*/

		    FeatureMap params = Factory.newFeatureMap(); // use default parameters
		    
		    // create PR for Tokeniser
		 	ProcessingResource pr = (ProcessingResource)Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR", params);
		 	// add the PR to the pipeline controller
		 	annieController.add(pr);
		    
		    // create PR for Tokeniser
		 	pr = (ProcessingResource)Factory.createResource("gate.creole.tokeniser.DefaultTokeniser", params);
			// add the PR to the pipeline controller
			annieController.add(pr);
		    
			// create PR for SentenceSplitter
			pr = (ProcessingResource)Factory.createResource("gate.creole.splitter.SentenceSplitter", params);
			// add the PR to the pipeline controller
			annieController.add(pr);
			
			
			
		    log.log(new LogRecord(Level.INFO, "annie initialization sucessful"));
		}
		catch (IOException e)
		{
			log.log(new LogRecord(Level.WARNING, "annie initialization failed: " + e.toString()));
			System.out.println("Error on creating file for gate plug-in");
			////e.printStackTrace();
		} 
		catch (ResourceInstantiationException e) 
		{
			log.log(new LogRecord(Level.WARNING, "annie initialization failed: " + e.toString()));
			System.out.println("Error on instantiating resource");
			////e.printStackTrace();
		} 
		catch (PersistenceException e) 
		{
			log.log(new LogRecord(Level.WARNING, "annie initialization failed: " + e.toString()));
			////e.printStackTrace();
		}
	}
	
	/**
	 * inits the MetaMap-processing resource and adds it to the annie-controller or an own instance of a MetaMapController
	 * depending on the configuration for metamap that is given by the user, the configuration is considered of the standard config is used
	 */
	private void InitMetaMapController()
	{
		log.log(new LogRecord(Level.INFO, "starting metamap initialization"));
		
		try 
		{
			if (metaMapConfiguration == null)
			{
				ProcessingResource metaMap = (ProcessingResource)Factory.createResource("gate.metamap.MetaMapPR", Factory.newFeatureMap());
				
				annieController.add(metaMap);
			}
			else
			{
				//included the gate-MetaMapPR-sources -> this makes it possible to instantiate the MetaMap-gate-plug-in directly, which in turn,
				//allows to configute the MetaMap plug-in, becuase it offers interfaces that are passed-through to the nlm-meta-map-Java-Api
				//the code line above instantiates the MetaMap directly from the lib using the string "gate.metamap.MetaMapPR" -> this does not
				ProcessingResource metaMap = (ProcessingResource)Factory.createResource("gate.metamap.MetaMapPR", Factory.newFeatureMap());
				
				MetaMapPR metaMapInstance = (MetaMapPR)metaMap;
				
				metaMapConfiguration.Configure(metaMapInstance);
				
				annieController.add(metaMapInstance);
			}
		} 
		catch (ResourceInstantiationException e) 
		{
			log.log(new LogRecord(Level.WARNING, "metamap initialization failed: " + e.toString()));
			System.out.println("Error on instantiating resource");
			//e.printStackTrace();
		}
		
	    log.log(new LogRecord(Level.INFO, "metamap initialization sucessful"));
	}
	
	/**
	 * inits a number-processing resource and adds it to the annie-controller or an own instance of a NumberController
	 */
	private void InitNumberController()
	{
		log.log(new LogRecord(Level.INFO, "starting tagger_numbers initialization"));
		
		try 
		{
			ProcessingResource numberTagger = (ProcessingResource)Factory.createResource("gate.creole.numbers.NumbersTagger", Factory.newFeatureMap());
			
			annieController.add(numberTagger);
		} 
		catch (ResourceInstantiationException e) 
		{
			log.log(new LogRecord(Level.WARNING, "tagger_numbers initialization failed: " + e.toString()));
			System.out.println("Error on instantiating resource");
			//e.printStackTrace();
		}
		
	    log.log(new LogRecord(Level.INFO, "tagger_numbers initialization sucessful"));
	}
	
	// controller of gate (in this case for annie, metamap and number tagger)
	private SerialAnalyserController annieController = null;
	
	// list of condition container (plain-text condition and ID)
	private Collection<ConditionExpression> conditionExpressions;
	// translator for translating annotated gate documents into logic conditions (of the Conditions package)
	private ConditionTranslator conditionTranslator;
	
	// Container for MetaMapConfiguration
	private MetaMapConfiguration metaMapConfiguration;
	
	//helper for the annotation logic; true if the last read token is an logic operator (AND, OR, XOR)
	private boolean isLastTokenALogicOperator = false;
	
	private static final Logger log = Logger.getLogger("GateController.java");
}
