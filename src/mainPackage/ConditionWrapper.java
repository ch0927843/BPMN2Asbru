package mainPackage;

import gate.metamap.OutputMode;
import gate.metamap.TaggerMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import AsbruConditions.FilterPrecondition;
import Conditions.ConditionBase;
import Conditions.Converter.ConditionConverter;

/**
 * this class holds an instance of the GateController class for handeling the gate features and MetaMap-controller
 * the methods of the GateController are called and the results are written into the output file
 * 
 * @author Christian Hinterer
 *
 */
public class ConditionWrapper 
{

	public ConditionWrapper()
	{
		this("C:\\Program Files\\GATE_Developer_7.1", null, null);
	}
	
	/**
	 * 
	 * @param gateHomeDir home directory of installed gate
	 * @param configFileName file name of the metamap configuration
	 * @param gateAppFileName file name of a gate application
	 */
	public ConditionWrapper(String gateHomeDir, String configFileName, String gateAppFileName)
	{
		if (configFileName != null && !configFileName.isEmpty())
		{
			parseConfigFile(configFileName);
		}
		
		if (!checkFileExistence(gateAppFileName))
		{
			gateAppFileName = "";
		}
		
		if (checkFileExistence(gateHomeDir))
		{
			createNewGateInstance(gateHomeDir, gateAppFileName);
		}
		else
		{
			createNewGateInstance("C:\\Program Files\\GATE_Developer_7.1", gateAppFileName);
		}
	}
	
	/**
	 * creates a new instance of the GateController
	 * @param gateHomeDir home directory of installed gate
	 * @param gateAppFileName file name of a gate application
	 */
	public void createNewGateInstance(String gateHomeDir, String gateAppFileName)
	{
		gateController = new GateController(gateHomeDir, metaMapConfiguration, gateAppFileName);
	}
	
	/**
	 * inits the gate system
	 */
	public void initGateComponents()
	{
		gateController.initGateComponents();
	}
	
	// start point of parser logic, returns true on success
	/**
	 * this is the key method of the wrapper! it uses the calls all necessary methods to process the input file
	 * and writes the found asbru conditions into a valid xml file
	 * 
	 * @param inputFileName file name of the input file
	 * @param outputFileName output file name
	 * @return returns true on sucess
	 */
	public boolean translate(String inputFileName, String outputFileName)
	{
		// list of conditions
		Collection<ConditionBase> conditions = new ArrayList<ConditionBase>();
		// list of the same conditions, but modelled in asbru
		Collection<FilterPrecondition> asbruConditions;
		
		// call for finding condition phrases in cpg (input) and processing them by annotating them with gate
		// and translating these annotated documents into logic conditions
		conditions = gateController.findAndProcessConditionExpressionStatements(inputFileName);
		// call for converting the found logic conditions into asbru conditions
		asbruConditions = convertConditions(conditions);
		
		// call for writing the asbru conditions into a file (valid xml)
		return writeAsbruConditionsToFile(asbruConditions, outputFileName);
	}
	
	/**
	 * takes the metamap configuration
	 * 
	 * @param metaMapConfiguration metamap configuration
	 */
	public void setMetaMapConfiguration(MetaMapConfiguration metaMapConfiguration)
	{
		this.metaMapConfiguration = metaMapConfiguration;
		gateController.setMetaMapConfiguration(metaMapConfiguration);
		initGateComponents();
	}
	
	/**
	 * parses the configuration file and writes the configuration into a container (the MetaMapConfiguration)
	 * @param configFileName
	 */
	public void parseConfigFile(String configFileName)
	{
		if (checkFileExistence(configFileName))
		{
			metaMapConfiguration = new MetaMapConfiguration();
			
			try
			{
				FileInputStream fstream = new FileInputStream(configFileName);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				String strLine;
				
				while ((strLine = br.readLine()) != null)   
				{
					String parameter = "";
					String value = "";
					
					parameter = strLine.substring(0, strLine.indexOf(":"));
					value = strLine.substring(strLine.indexOf(":") + 1, strLine.length());
					
					setMetaMapConfigurationOption(parameter, value);
				}
					
				in.close();
				
				if (gateController != null)
				{
					gateController.setMetaMapConfiguration(metaMapConfiguration);	
				}
			}
			catch (FileNotFoundException e)
			{
				log.log(new LogRecord(Level.WARNING, "configuration file not found: " + e.toString()));
				System.out.println("configuration file not found");
			}
			catch (IOException e)
			{
				log.log(new LogRecord(Level.WARNING, "error on reading configuration file: " + e.toString()));
				System.out.println("error on reading configuration file");
			}
		}
		else
		{
			System.out.println("configurationFile does not exist!");
		}
	}
	
	/**
	 * sets the gate application file in the gateController
	 * @param gateAppFile gate application file
	 */
	public void setGateApplicationFile(String gateAppFile)
	{
		if (checkFileExistence(gateAppFile))
		{
			if (gateController != null)
			{
				gateController.setGateApplicationFile(gateAppFile);
			}
			else
			{
				System.out.println("gateController not initialized!");
			}
		}
		else
		{
			System.out.println("gate application file does not exist!");
		}
	}
	
	/**
	 * uses the converter to convert classes containing logic conditions (of the Conditions package)
	 * into asbru conditions (AsbruConditions package)
	 * @param conditions classes containing conditions
	 * @return classes containing the input conditions (parameter) modelled in asbru
	 */
	private Collection<FilterPrecondition> convertConditions(Collection<ConditionBase> conditions)
	{
		Collection<FilterPrecondition> asbruConditions = new ArrayList<FilterPrecondition>();
		
		asbruConditions = ConditionConverter.convert(conditions);
		
		return asbruConditions;
	}
	
	/**
	 * writes the found conditions moddeled in asbru into a file (valid xml)
	 * 
	 * @param asbruConditions the asbru conditions
	 * @param fileName the file name
	 * @return true on sucess
	 */
	private boolean writeAsbruConditionsToFile(Collection<FilterPrecondition> asbruConditions, String fileName)
	{
		int i = 1;
		
		try
		{
			if (fileName == null || fileName.isEmpty())
			{
				fileName = "output.xml";
			}
			else
			{
				if (fileName.length() < 4)
				{
					fileName = fileName + ".xml";
				}
				else
				{
					if (!fileName.substring(fileName.length() - 4, fileName.length()).equals(".xml"))
					{
						//fileName = fileName.substring(0, fileName.length() - 4) + ".xml";
						fileName = fileName + ".xml";
					}
				}
			}
			
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);

			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.write("\n");
			//out.write("<!DOCTYPE plan-library PUBLIC \"AsbuDTD\" \"C:/PROJECT/Asbru_7.3h_DELTA_modified.dtd\">");
			out.write("<!DOCTYPE plan-library PUBLIC \"AsbuDTD\" \"Asbru_7.3h_DELTA_modified.dtd\">");
			out.write("\n");
			out.write("<plan-library>");
			out.write("\n");
			out.write("<plans>");
			out.write("\n");
			out.write("<plan-group>");
			out.write("\n");
			
			for(FilterPrecondition condition: asbruConditions)
			{
				out.write("<plan name=\"plan" + String.valueOf(i) + "\">");
				out.write("\n");
				out.write("<conditions>");
				out.write("\n");
				out.write(condition.print());
				out.write("\n");
				out.write("</conditions>");
				out.write("\n");
				out.write("</plan>");
				out.write("\n");
				
				i++;
			}
			
			out.write("</plan-group>");
			out.write("\n");
			out.write("</plans>");
			out.write("\n");
			out.write("</plan-library>");
			out.write("\n");
			
			out.close();
			fstream.close();
			
			return true;
		}
		catch (IOException e)
		{
			log.log(new LogRecord(Level.WARNING, "writing to file failed: " + e.toString()));
			System.out.println("Error on writing to file!");
			return false;
		}
	}
	 
	/**
	 * could possibly be solved with reflaction, but I do not know if and how java supports the usage of reflection
	 * or using a config file modelled in xml
	 * very quick and dirty solution using if statements
	 * 
	 * @param parameter string of configuration file containing some sort of configuration option for metamap
	 * @param value string of configuration file containing some sort of configuration value for metamp
	 */
	private void setMetaMapConfigurationOption(String parameter, String value)
	{
		if(parameter.equals("host"))
		{
			metaMapConfiguration.setHost(value);
		}
		else if(parameter.equals("port"))
		{
			metaMapConfiguration.setPort(Integer.parseInt(value));
		}
		else if(parameter.equals("annotateNegEx"))
		{
			if (value.equals("false"))
			{
				metaMapConfiguration.setAnnotateNegEx(false);	
			}
			else
			{
				metaMapConfiguration.setAnnotateNegEx(true);
			}
		}
		else if(parameter.equals("annotatePhrases"))
		{
			if (value.equals("false"))
			{
				metaMapConfiguration.setAnnotatePhrases(false);	
			}
			else
			{
				metaMapConfiguration.setAnnotatePhrases(true);
			}
		}
		else if(parameter.equals("inputASName"))
		{
			metaMapConfiguration.setInputASName(value);
		}
		else if(parameter.equals("inputASTypes"))
		{
			ArrayList<String> types = new ArrayList<String>();
			
			int idx = -1;
			
			while((idx = value.indexOf(",")) != -1)
			{
				String type = value.substring(0, idx);
				types.add(type);
				value = value.substring(idx + 1, value.length());
			}
				
			metaMapConfiguration.setInputASTypes(types);
		}
		else if(parameter.equals("inputASTypeFeature"))
		{
			metaMapConfiguration.setInputASTypeFeature(value);
		}
		else if(parameter.equals("metaMapOptions"))
		{
			metaMapConfiguration.setMetaMapOptions(value);
		}
		else if(parameter.equals("outputASName"))
		{
			metaMapConfiguration.setOutputASName(value);
		}
		else if(parameter.equals("outputASType"))
		{
			metaMapConfiguration.setOutputASType(value);
		}
		else if(parameter.equals("outputMode"))
		{
			if (value.equals(OutputMode.AllCandidates.toString()))
			{
				metaMapConfiguration.setOutputMode(OutputMode.AllCandidates);
			}
			else if (value.equals(OutputMode.AllCandidatesAndMappings.toString()))
			{
				metaMapConfiguration.setOutputMode(OutputMode.AllCandidatesAndMappings);
			}
			else if (value.equals(OutputMode.AllMappings.toString()))
			{
				metaMapConfiguration.setOutputMode(OutputMode.AllMappings);
			}
			else if (value.equals(OutputMode.HighestMappingLowestCUI.toString()))
			{
				metaMapConfiguration.setOutputMode(OutputMode.HighestMappingLowestCUI);
			}
			else if (value.equals(OutputMode.HighestMappingMostSources.toString()))
			{
				metaMapConfiguration.setOutputMode(OutputMode.HighestMappingMostSources);
			}
			else if (value.equals(OutputMode.HighestMappingOnly.toString()))
			{
				metaMapConfiguration.setOutputMode(OutputMode.HighestMappingOnly);
			}
		}
		else if(parameter.equals("taggerMode"))
		{
			if (value.equals(TaggerMode.AllOccurrences.toString()))
			{
				metaMapConfiguration.setTaggerMode(TaggerMode.AllOccurrences);
			}
			else if (value.equals(TaggerMode.CoReference.toString()))
			{
				metaMapConfiguration.setTaggerMode(TaggerMode.CoReference);
			}
			else if (value.equals(TaggerMode.FirstOccurrenceOnly.toString()))
			{
				metaMapConfiguration.setTaggerMode(TaggerMode.FirstOccurrenceOnly);
			}
		}
	}
	
	/**
	 * checks if a file with the given name exists
	 * 
	 * @param fileName: file-name for existence check
	 * @return true on existence or false
	 */
	private boolean checkFileExistence(String fileName)
	{
		File f = new File(fileName);
		
		return f.exists();
	}
	
	// instance of the gate controller
	private GateController gateController;
	
	// container for metamap configuration
	private MetaMapConfiguration metaMapConfiguration = null;
	
	private static final Logger log = Logger.getLogger("ConditionWrapper.java");
}
