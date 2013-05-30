project title: BPMN2Asbru

author: Christian Hinterer

date: May 2013

external libraries:
	- Gate JAVA API: Gate Version 7.0 Build 4195; no licence required 
		(downloaded at http://gate.ac.uk/download/)
	- MetaMap JAVA API: MetaMap Version 2012; registration required -> i got an account of my supervisor Mag. Dr.rer.soc.oec. Katharina Kaiser
		(downloaded at http://www.nlm.nih.gov/research/umls/) -> the exact link requires login

external components:
	- MetaMap 2012: i run my own server instances of (wsdserverctl and skrmedpostctl and mmserver12)
		(downloaded at http://www.nlm.nih.gov/research/umls/) -> the exact link requires login

operating system: Windows 7

development environment: JAVA SE Development Kit 6 Update 37
	note: important to use JDK 6 (Java 1.6) -> otherwise the usage of the gate JAVA API, respectively the MetaMap
		JAVA API won't work properly

integrated development environment: Eclipse SDK Version: 4.2.1 Build id: M20120914-1800

how to start the program: 
	- either with the Eclipse IDE (open the whole project and run it from the environment)
	- or start the jar file (BPMN2Asbru.jar)

name of the executable program file: BPMN2Asbru.jar

command line parameters:
	1. name of the input file (the input file has to be in valid xml) -> optional
	2. name of the output file -> optional
	3. name of the file that includes the MetaMap configuration -> optional
	4. gate home directory
note: you either can use one of the following scenarios:
	1. no command line parameters
	2. command line parameter 1
	3. command line parameters 1 and 2
	4. command line parameters 1, 2 and 3
	5. command line parameters 1, 2, 3 and 4

configuration files and settings:
	despite using the standard MetaMap configuration, you have 2 possibilities to hand over your own configuration
		1. command line parameter number 3
		2. using the menue at runtime to choose a configuration file
			note: a configuration (MetaMapConfig.cfg) file and a template (MetaMap.Config_regExp.txt -> regular expression), 
				respectively a description are given in the data folder of the project (../BPMN2Asbru/data/MetaMap.Config_regExp.txt) 
				and (../BPMN2Asbru/data/MetaMapConfig.cfg) for more details read
				http://gate.ac.uk/sale/tao/splitch16.html#x21-40900016.1.2



(../BPMN2Asbru/Data/MetaMap.Config_regExp.txt) and (../BPMN2Asbru/data/MetaMapConfig.cfg)