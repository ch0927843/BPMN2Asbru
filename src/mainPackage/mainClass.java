package mainPackage;

import java.io.File;
import java.util.Scanner;
import java.util.logging.*;

import org.apache.log4j.BasicConfigurator;

/**
 * console application to test the Condition-Wrapper 
 * @author Christian Hinterer
 *
 */
public class mainClass 
{
	
	/**
	 * the main method reads the command line parameters, creates a new instance of the conditionWrapper, reads the user input and
	 * calls methods, depending on the user input
	 * 
	 * @param args [ input file name [ output file name [ configuration file name [ gate home directory ] ] ] ]
	 */
	public static void main(String[] args) 
	{
		BasicConfigurator.configure();
		log.log(new LogRecord(Level.INFO, "programm started"));
		
		int command = -1;
		
		if (args.length > 0)
		{
			if (CheckFileExistence(args[0]))
			{
				inputFileName = args[0];
			}
			else
			{
				System.out.println("inputFile defined by commandline argument 'one' does not exist!");
			}
		}
		if (args.length > 1)
		{
			gateAppFileName = args[1];
		}
		if (args.length > 2)
		{
			configFileName = args[2];
		}
		if (args.length > 3)
		{
			outputFileName = args[3];
		}
		if (args.length > 4)
		{
			gateHomeDir = args[4];
		}
		
		// creating new instance of the conditionWrapper
		ConditionWrapper conditionWrapper = new ConditionWrapper(gateHomeDir, configFileName, gateAppFileName);
		
		while (command != 8)
		{
			printMenue();
			
			Scanner scanner = new Scanner(System.in);
			command = scanner.nextInt();
			
			// distinguish the user input
			switch (command) 
			{
				case 1:
					System.out.print("Enter input file: ");
					inputFileName = ReadFileNameFromConsole(FileNameType.inputFile);
					break;
				case 2:
					if (!inputFileName.isEmpty())
					{
						if (CheckFileExistence(inputFileName) && !inputFileName.equals(""))
						{
							if (!conditionWrapper.Translate(inputFileName, outputFileName))
							{
								System.out.println("Error! Please read the log!");
							}
						}
						else
						{
							System.out.println("inputFile does not exist!");
						}
					}
					else
					{
						System.out.println("inputFile must not be null or empty!");
					}
					break;
				case 3:
					System.out.print("Enter gate application file: ");
					gateAppFileName = ReadFileNameFromConsole(FileNameType.gateAppFile);
					if (gateAppFileName != null && !gateAppFileName.equals(""))
					{
						conditionWrapper.SetGateApplicationFile(gateAppFileName);
					}
					break;
				case 4:
					System.out.print("Enter configuration file: ");
					configFileName = ReadFileNameFromConsole(FileNameType.configFile);
					if (configFileName != null && !configFileName.equals(""))
					{
						conditionWrapper.ParseConfigFile(configFileName);
					}
					break;
				case 5:
					System.out.print("Enter output file: ");
					outputFileName = ReadFileNameFromConsole(FileNameType.outputFile);
					break;
				case 6:
					System.out.print("Enter gate home directory: ");
					gateHomeDir = ReadFileNameFromConsole(FileNameType.outputFile); //dirty fix: same behavior like output file
					conditionWrapper.CreateNewGateInstance(gateHomeDir, gateAppFileName);
					break;
				case 7:
					conditionWrapper.InitGateComponents();
					break;
				case 8:
					log.log(new LogRecord(Level.INFO, "programm terminated"));
					scanner.close();
					System.exit(0);
					break;
				default:
					System.out.println("Invalid command!");
			}
		}
	}
	
	/**
	 * prints a menue to give options to the user
	 */
	private static void printMenue()
	{
		// testing purposes...
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("");
		System.out.println("-----------------------------");
		System.out.println("+++++++++ M E N U E +++++++++");
		System.out.println("-----------------------------");
		System.out.println("Change input file..............1");
		System.out.println("PROCESS input to output........2");
		System.out.println("Change gate application file:..3");
		System.out.println("Change config file.............4");
		System.out.println("Change output file.............5");
		System.out.println("Reset gate-home directory......6");
		System.out.println("Re-initialize gate.............7");
		System.out.println("Quit...........................8");
		System.out.print("Choice........................:");
	}

	/**
	 * reads a string from the console, that contains a file name
	 * 
	 * @param fileNameType input file | output file | configuration file
	 * @return returns the string (file name) read from console
	 */
	private static String ReadFileNameFromConsole(FileNameType fileNameType)
	{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.nextLine();
		
		if (fileNameType == FileNameType.inputFile)
		{
			if(CheckFileExistence(fileName)) 
			{ 
				return fileName;  
			}
			else
			{
				System.out.println("File does not exist!");
				return "";
			}
		}
		else if (fileNameType == FileNameType.outputFile)
		{
			return fileName;
		}
		else if (fileNameType == FileNameType.configFile)
		{
			if(CheckFileExistence(fileName)) 
			{ 
				return fileName;  
			}
			else
			{
				System.out.println("File does not exist!");
				return "";
			}
		}
		else if (fileNameType == FileNameType.gateAppFile)
		{
			if(CheckFileExistence(fileName)) 
			{ 
				return fileName;  
			}
			else
			{
				System.out.println("File does not exist!");
				return "";
			}
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param fileName: file name for existence check
	 * @return true on existence or false if not existing
	 */
	private static boolean CheckFileExistence(String fileName)
	{
		File f = new File(fileName);
		
		return f.exists();
	}
	
	// input file (CPG) and output file (Asbru language)
	private static String inputFileName = "";
	// output file name
	private static String outputFileName = "";
	// configuration file name;
	private static String configFileName = "";
	// home directory of gate application
	private static String gateHomeDir = "";
	// file name of a gate application
	private static String gateAppFileName = "";
	
	private static final Logger log = Logger.getLogger("mainClass.java");
}
