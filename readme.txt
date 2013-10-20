project title: BPMN2Asbru

author: Christian Hinterer

date: May 2013

external libraries:
	- Gate JAVA API: Gate Version 7.1; no licence required 
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

list of command line parameters (all parameters except of the input file are optional); examples are in brackets:
	-input:filename [-input:inputfilename.xml] explanation: filename of the file with the content to be processed
	-output:filename [-output:outputfilename] explanation: filename of the file that is created with the programm output
	-mmconf:filname [-mmconf:MetaMapConfig.cfg] explanation: filename of the file that contains configurations (options) for MetaMap (see the last section of this file "configuration files and settings")
	-gateapp:filename [-gateapp:gateGUIexport.gapp] explanation: you can hand over your specific already with GateDeveloper created "corpus pipeline" instead of using that one that is created in the code by me
	-gatehome:directoryname [-gatehome:C:\gate] explanation: if you do not use this argument to specify the installation folder of your gate, C:\Program Files\GATE_Developer_7.1 is used

configuration files and settings:
	a configuration (MetaMapConfig.cfg) file and a template (MetaMap.Config_regExp.txt -> regular expression), 
	respectively a description are given in the data folder of the project (../BPMN2Asbru/data/MetaMap.Config_regExp.txt) 
	and (../BPMN2Asbru/data/MetaMapConfig.cfg) for more details read
	http://gate.ac.uk/sale/tao/splitch16.html#x21-40900016.1.2

