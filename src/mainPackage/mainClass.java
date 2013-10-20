package mainPackage;

import java.io.File;
//import java.util.Scanner;
import java.util.logging.*;

//import org.apache.log4j.BasicConfigurator;


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
		//BasicConfigurator.configure();
		log.log(new LogRecord(Level.INFO, "programm started"));
		
		//int command = -1;
		
		for (int i = 0; i < args.length; i++)
		{
			processCommandLineArgument(args[i]);
		}

		if (!inputFileName.equals("") && inputFileName != null && !inputFileName.isEmpty())
		{
			//creating new instance of the conditionWrapper
			ConditionWrapper conditionWrapper = new ConditionWrapper(gateHomeDir, configFileName, gateAppFileName);
			
			if (!conditionWrapper.translate(inputFileName, outputFileName))
			{
				System.out.println("Error! Please read the log!");
			}
		}
		else
		{
			System.out.println("inputFileName must not be null or empty");
		}
		
		
		/*while (command != 2)
		{
			printMenue();
			
			Scanner scanner = new Scanner(System.in);
			command = scanner.nextInt();
			
			// distinguish the user input
			switch (command) 
			{		
				case 1:
					System.out.print("Enter input file: ");
					inputFileName = readFileNameFromConsole(FileNameType.inputFile);
					break;
				case 2:
					if (!inputFileName.isEmpty())
					{
						if (checkFileExistence(inputFileName) && !inputFileName.equals(""))
						{
							if (!conditionWrapper.translate(inputFileName, outputFileName))
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
					gateAppFileName = readFileNameFromConsole(FileNameType.gateAppFile);
					if (gateAppFileName != null && !gateAppFileName.equals(""))
					{
						conditionWrapper.setGateApplicationFile(gateAppFileName);
					}
					break;
				case 4:
					System.out.print("Enter configuration file: ");
					configFileName = readFileNameFromConsole(FileNameType.configFile);
					if (configFileName != null && !configFileName.equals(""))
					{
						conditionWrapper.parseConfigFile(configFileName);
					}
					break;
				case 5:
					System.out.print("Enter output file: ");
					outputFileName = readFileNameFromConsole(FileNameType.outputFile);
					break;
				case 6:
					System.out.print("Enter gate home directory: ");
					gateHomeDir = readFileNameFromConsole(FileNameType.outputFile); //dirty fix: same behavior like output file
					conditionWrapper.createNewGateInstance(gateHomeDir, gateAppFileName);
					break;
				case 7:
					conditionWrapper.initGateComponents();
					break;
				case 8:
					log.log(new LogRecord(Level.INFO, "programm terminated"));
					scanner.close();
					System.exit(0);
					break;
				default:
					System.out.println("Invalid command!");
			}
		}*/
	}
	
	/**
	 * prints a menue to give options to the user
	 */
	/*private static void printMenue()
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
		System.out.println("process new input file.......1");
		System.out.println("Quit.........................2");
		System.out.print("Choice......................:");
	}*/

	/**
	 * reads a string from the console, that contains a file name
	 * 
	 * @param fileNameType input file | output file | configuration file
	 * @return returns the string (file name) read from console
	 */
	/*private static String readFileNameFromConsole(FileNameType fileNameType)
	{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.nextLine();
		
		if (fileNameType == FileNameType.inputFile)
		{
			if(checkFileExistence(fileName)) 
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
			if(checkFileExistence(fileName)) 
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
			if(checkFileExistence(fileName)) 
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
	}*/
	
	/**
	 * 
	 * @param commandLineargument command line argument
	 */
	private static void processCommandLineArgument(String commandLineargument)
	{
		String[] arg = commandLineargument.split(":", 2);
		
		if (arg.length == 2)
		{
			String command = arg[0].toString().substring(1, arg[0].toString().length());
			String argument = arg[1].toString();
			
			if (!command.equals("") && command != null && !command.isEmpty() && command.equals("output"))
			{
				if (!argument.equals("") && argument != null && !argument.isEmpty())
				{
					outputFileName = argument;
					return;
				}
			}
			
			if (!argument.equals("") && argument != null && !argument.isEmpty() && checkFileExistence(argument))
			{
				if (!command.equals("") && command != null && !command.isEmpty())
				{
					if (command.equals("input"))
					{
						inputFileName = argument;
					}
					else if (command.equals("mmconf"))
					{
						configFileName = argument;
					}
					else if (command.equals("gateapp"))
					{
						gateAppFileName = argument;
					}
					else if (command.equals("gatehome"))
					{
						gateHomeDir = argument;
					}
					else
					{
						System.out.println("command does not exist");
					}
				}
				else
				{
					System.out.println(command + " must not be null or empty");
				}
			}
			else
			{
				System.out.println(argument + " does not exist");
			}
		}
		else
		{
			System.out.println("wrong argument: " + commandLineargument);
		}
	}
	
	/**
	 * 
	 * @param fileName: file name for existence check
	 * @return true on existence or false if not existing
	 */
	private static boolean checkFileExistence(String fileName)
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
