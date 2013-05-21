package mainPackage;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.util.InvalidOffsetException;
import Conditions.CombinedCondition;
import Conditions.ConditionBase;
import Conditions.LogicOperatorType;
import Conditions.VariableUnknownCondition;

public class ConditionTranslatorJAPE {

	/**
	 * initializes the statistics (clears it)
	 */
	public ConditionTranslatorJAPE()
	{
		InitStatistics();
	}
	
	public ConditionBase Translate(Document document, String id)
	{
		ConditionBase actualCondition = null;
		
		/*try
		{
			
			return actualCondition;
		}
		catch (InvalidOffsetException e)
		{
			log.log(new LogRecord(Level.WARNING, "error on translating (invalid offset): " + e.toString()));
			System.out.println("An invalid offset was used while translating!");
			
			actualCondition = new VariableUnknownCondition("DUMMY", id);
			actualCondition.AddComment("error on translating (invalid offset)" + e.toString());
			
			return actualCondition;
		}*/
		
		return actualCondition;
	}
	
	/**
	 * prints a statistic of the found conditions
	 */
	public void PrintStatistics()
	{
		allConditions = comparison + hasOccured + isAtStart + isAtEnd + isKnownVariable + isUnknownVariable;
		allConditions = allConditions +	isMemberOf + isWithinRange + simpleConditionCombination + simpleConditionNot;
		
		System.out.println("* * * * * STATISTIC OF TRANSLATED CONDITIONS * * * * *");
		System.out.println("Total conditions found: " + allConditions);
		System.out.println();
		System.out.println("These translated conditions are segmented on following condition types");
		System.out.println();
		System.out.println("comparisons: " + comparison);
		System.out.println("hasOccured: " + hasOccured);
		System.out.println("isAtStart: " + isAtStart);
		System.out.println("isAtEnd: " + isAtEnd);
		System.out.println("isKnownVariable: " + isKnownVariable);
		System.out.println("isUnknownVariable: " + isUnknownVariable);
		System.out.println("isMemberOf: " + isMemberOf);
		System.out.println("isWithinRange: " + isWithinRange);
		System.out.println("simpleConditionCombination: " + simpleConditionCombination);
		System.out.println("simpleConditionNot: " + simpleConditionNot);
		System.out.println("");
	}
	
	/**
	 * clears the statistics
	 */
	private void InitStatistics()
	{
		allConditions = 0;
		comparison = 0;
		hasOccured = 0;
		isAtStart = 0;
		isAtEnd = 0;
		isKnownVariable = 0;
		isUnknownVariable = 0;
		isMemberOf = 0;
		isWithinRange = 0;
		simpleConditionCombination = 0;
		simpleConditionNot = 0;
	}
	
	// counters for the found condition types
	private int allConditions;
	private int comparison;
	private int hasOccured;
	private int isAtStart;
	private int isAtEnd;
	private int isKnownVariable;
	private int isUnknownVariable;
	private int isMemberOf;
	private int isWithinRange;
	private int simpleConditionCombination;
	private int simpleConditionNot;
	
	private static final Logger log = Logger.getLogger("ConditionTranslatorJAPE.java");
}
