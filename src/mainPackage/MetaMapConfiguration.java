package mainPackage;

import java.util.ArrayList;

import gate.metamap.OutputMode;
import gate.metamap.TaggerMode;

/**
 * this class is a container for metamap configurations. All options configured by a user via a configuration file
 * are hold in an instance of this class
 * <p>
 * more information about the configuration options can be found at:
 * http://gate.ac.uk/sale/tao/splitch16.html#sec:misc-creole:metamap
 * @author Christian Hinterer
 */
public class MetaMapConfiguration {

	public MetaMapConfiguration()
	{
		host = null;
		port = -1;
		annotateNegEx = false;
		annotatePhrases = false;
		inputASName = null;
		inputASTypes = new ArrayList<String>();
		inputASTypeFeature = null;
		metaMapOptions = null;
		outputASName = null;
		outputASType = null;
		outputMode = OutputMode.AllMappings;
		taggerMode = TaggerMode.FirstOccurrenceOnly;
	}
	
	/**
	 * This method configures an instance of a metamap-processing-ressource with the configuration-options defined by a user
	 * @param metaMap metamap-processing-ressource
	 */
	public void configure(gate.metamap.MetaMapPR metaMap)
	{
		try 
		{
/*			if (host != null && !host.isEmpty() && port != -1)
			{
				metaMap.setHost(host);
				metaMap.setPort(port);
			}
			else
			{
				metaMap.setHost("localhost");
				metaMap.setPort(8066);
			}*/
			
			metaMap.setAnnotateNegEx(annotateNegEx);
			metaMap.setAnnotatePhrases(annotatePhrases);
			
			if (inputASName != null && !inputASName.isEmpty())
			{
				metaMap.setInputASName(inputASName);
			}
			
			if (inputASTypes != null && !inputASTypes.isEmpty())
			{
				metaMap.setInputASTypes(inputASTypes);
			}
			
			if (inputASTypeFeature != null && !inputASTypeFeature.isEmpty())
			{
				metaMap.setInputASTypeFeature(inputASTypeFeature);
			}
			
			if (metaMapOptions != null && !metaMapOptions.isEmpty())
			{
				metaMap.setMetaMapOptions(metaMapOptions);
			}
			
			if (outputASName != null && !outputASName.isEmpty())
			{
				metaMap.setOutputASName(outputASName);
			}
			
			if (outputASType != null && !outputASType.isEmpty())
			{
				metaMap.setOutputASType(outputASType);
			}
			
			metaMap.setOutputMode(outputMode);
			metaMap.setTaggerMode(taggerMode);
		}
		catch (Exception e) 
		{
			System.out.println("Error on setting up MetaMap with host: " + host + " and port: " + port);
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return host
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * 
	 * @param host
	 */
	public void setHost(String host)
	{
		this.host = host;
	}
	
	/**
	 * 
	 * @return port
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * 
	 * @param port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}
	
	/**
	 * 
	 * @return annotateNegEx
	 */
	public boolean getAnnotateNegEx()
	{
		return annotateNegEx;
	}
	
	/**
	 * 
	 * @param annotateNegEx
	 */
	public void setAnnotateNegEx(boolean annotateNegEx)
	{
		this.annotateNegEx = annotateNegEx;
	}
	
	/**
	 * 
	 * @return annotatePhrases
	 */
	public boolean getAnnotatePhrases()
	{
		return annotatePhrases;
	}
	
	/**
	 * 
	 * @param annotatePhrases
	 */
	public void setAnnotatePhrases(boolean annotatePhrases)
	{
		this.annotatePhrases = annotatePhrases;
	}
	
	/**
	 * 
	 * @return inputASName
	 */
	public String getInputASName()
	{
		return inputASName;
	}
	
	/**
	 * 
	 * @param inputASName
	 */
	public void setInputASName(String inputASName)
	{
		this.inputASName = inputASName;
	}
	
	/**
	 * 
	 * @return inputASTypes
	 */
	public ArrayList<String> getInputASTypes()
	{
		return inputASTypes;
	}
	
	/**
	 * 
	 * @param inputASTypes
	 */
	public void setInputASTypes(ArrayList<String> inputASTypes)
	{
		this.inputASTypes = inputASTypes;
	}
	
	/**
	 * 
	 * @param inputASType
	 */
	public void addInputASType(String inputASType)
	{
		this.inputASTypes.add(inputASType);
	}
	
	/**
	 * 
	 * @param inputASType
	 * @return sucess
	 */
	public boolean removeInputASType(String inputASType)
	{
		return this.inputASTypes.remove(inputASType);
	}
	
	/**
	 * 
	 * @return inputASTypeFeature
	 */
	public String getInputASTypeFeature()
	{
		return inputASTypeFeature;
	}
	
	/**
	 * 
	 * @param inputASTypeFeature
	 */
	public void setInputASTypeFeature(String inputASTypeFeature)
	{
		this.inputASTypeFeature = inputASTypeFeature;
	}
	
	/**
	 * 
	 * @return metaMapOptions
	 */
	public String getMetaMapOptions()
	{
		return metaMapOptions;
	}
	
	/**
	 * 
	 * @param metaMapOptions
	 */
	public void setMetaMapOptions(String metaMapOptions)
	{
		this.metaMapOptions = metaMapOptions;
	}
	
	/**
	 * 
	 * @return outputASName
	 */
	public String getOutputASName()
	{
		return outputASName;
	}
	
	/**
	 * 
	 * @param outputASName
	 */
	public void setOutputASName(String outputASName)
	{
		this.outputASName = outputASName;
	}
	
	/**
	 * 
	 * @return outputASType
	 */
	public String getOutputASType()
	{
		return outputASType;
	}
	
	/**
	 * 
	 * @param outputASType
	 */
	public void setOutputASType(String outputASType)
	{
		this.outputASType = outputASType;
	}
	
	/**
	 * 
	 * @return outputMode
	 */
	public OutputMode getOutputMode()
	{
		return outputMode;
	}
	
	/**
	 * 
	 * @param outputMode
	 */
	public void setOutputMode(OutputMode outputMode)
	{
		this.outputMode = outputMode;
	}
	
	/**
	 * 
	 * @return taggerMode
	 */
	public TaggerMode getTaggerMode()
	{
		return taggerMode;
	}
	
	/**
	 * 
	 * @param taggerMode
	 */
	public void setTaggerMode(TaggerMode taggerMode)
	{
		this.taggerMode = taggerMode;
	}
	
	// metaMap configuration options
	// host of the metamap server
	private String host;
	// port of the metamap server
	private int port;
	private boolean annotateNegEx;
	private boolean annotatePhrases;
	private  String inputASName;
	private  ArrayList<String> inputASTypes;
	private  String inputASTypeFeature;
	private  String metaMapOptions;
	private  String outputASName;
	private  String outputASType;
	private  OutputMode outputMode;
	private  TaggerMode taggerMode;
}
