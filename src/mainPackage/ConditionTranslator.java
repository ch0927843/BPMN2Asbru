package mainPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.util.InvalidOffsetException;
import Conditions.CombinedCondition;
import Conditions.ComparisonCondition;
import Conditions.ConditionBase;
import Conditions.ContainsCondition;
import Conditions.LogicOperatorType;
import Conditions.MatchCode;
import Conditions.NotCondition;
import Conditions.RangeCondition;
import Conditions.VariableUnknownCondition;

/**
 * This class gets an annotated gate-Document and filters logic conditions 
 * and creates the necessary container condition classes of the package conditions with
 * the found informations.
 * Every condition containes the plain-text (in which it was found) as comment
 * 
 * @author Christian Hinterer
 */
public class ConditionTranslator
{
	/**
	 * initializes the statistics (clears it)
	 */
	public ConditionTranslator()
	{
		InitStatistics();
	}
	
	/**
	 * For finding conditions in annotated gate documents and translating them
	 * into container classes for conditions.
	 * This method is the entry point of the translation logic and therefore
	 * the only public method of this class that translated conditions.
	 * This method solves nested brace expressions and splits them into
	 * logic combinations (AND, OR XOR) of conditions.
	 * 
	 * @param document annotated gate document containing conditions in plain-text
	 * @param id ID to identify the conditions
	 * @return returns an object containing some sort of logic condition
	 */
	public ConditionBase Translate(Document document, String id)
	{
		this.document = document;
		this.docContent = document.getContent();
		this.stringContent = docContent.toString();
		AnnotationSet allMarkupsAS = document.getAnnotations();
		
		ConditionBase actualCondition = null;
		ConditionBase innerCondition = null;
		long innerConditionStartOffset = 0;
		//long innerConditionEndOffset = 0;
		
		try
		{
			while (logicBraceLayerCount > 0) // all nested braces
			{
				AnnotationSet logicBraceAS = allMarkupsAS.get("logicBraceContentLayer" + logicBraceLayerCount);
				
				Iterator<Annotation> logicBraceIterator = logicBraceAS.iterator();
				
				if (logicBraceIterator.hasNext()) // only one logic brace for each layer considered at the moment
				{
					Annotation logicBrace = logicBraceIterator.next();
	
					if (innerCondition == null) // on the most inner brace the innerCondition is null
					{
						actualCondition = TranslateConditionCombination(logicBrace.getStartNode().getOffset(), logicBrace.getEndNode().getOffset());
					}
					else // otherwise the actual condition of the actual brace depth is combined with the depper (more inner) found conditions
					{
						long currentStartOffset = logicBrace.getStartNode().getOffset();
						long currentEndOffset = innerConditionStartOffset - 5;
						
						ConditionBase conditionLeft = TranslateConditionCombination(currentStartOffset, currentEndOffset);
						
						AnnotationSet operator = allMarkupsAS.get("logicOperator", currentEndOffset, innerConditionStartOffset);
						LogicOperatorType logicOperatorType = GetLogicOperatorFromString(docContent.getContent(operator.firstNode().getOffset(), operator.lastNode().getOffset()).toString());
	
						actualCondition = new CombinedCondition(conditionLeft, logicOperatorType, "");
						((CombinedCondition) actualCondition).AddCondition(innerCondition);
						simpleConditionCombination++;
						
						actualCondition.AddComment(docContent.getContent(logicBrace.getStartNode().getOffset(), logicBrace.getEndNode().getOffset()).toString());
					}
					
					innerCondition = actualCondition;
					innerConditionStartOffset = logicBrace.getStartNode().getOffset();
				}
				
				logicBraceLayerCount--;
			}
			
			if (innerCondition != null) // if the document contains any brace structure, the conditions found in braces are combined with the conditions outside of the most outer brace
			{
				ConditionBase conditionLeft = TranslateConditionCombination(0, innerConditionStartOffset - 5);
				AnnotationSet operator = allMarkupsAS.get("logicOperator", innerConditionStartOffset - 5, innerConditionStartOffset);
				LogicOperatorType logicOperatorType = GetLogicOperatorFromString(docContent.getContent(operator.firstNode().getOffset(), operator.lastNode().getOffset()).toString());
	
				actualCondition = new CombinedCondition(conditionLeft, logicOperatorType, "");
				((CombinedCondition) actualCondition).AddCondition(innerCondition);
				simpleConditionCombination++;
				
				actualCondition.AddComment(stringContent);
			}
			else // if the document does not contain any "logic" braces...
			{
				if (allMarkupsAS.get("colon").isEmpty()) //...and no conditions seperated by colons...
				{
					actualCondition = TranslateConditionCombination(0, stringContent.length()); //...the method to search logic operators is called
				}
				else //...but colons are included...
				{
					actualCondition = TranslateConditionOtherThanLogicCombination(0, stringContent.length()); //...the method to distinguish between all other (except logic combined) conditions are called
				}
			}
			
			actualCondition.SetID(id);
			
			return actualCondition;
		}
		catch (InvalidOffsetException e)
		{
			log.log(new LogRecord(Level.WARNING, "error on translating (invalid offset): " + e.toString()));
			System.out.println("An invalid offset was used while translating!");
			
			actualCondition = new VariableUnknownCondition("DUMMY", id);
			actualCondition.AddComment("error on translating (invalid offset)" + e.toString());
			
			return actualCondition;
		}
	}
				
	/**
	 * increments the count ot the brace structure depth
	 */
	public void IncrementLogicBraceLayerCount()
	{
		logicBraceLayerCount++;
	}
	
	/**
	 * resets the count of the brace structure depth
	 */
	public void ResetLogicBraceLayerCount()
	{
		logicBraceLayerCount = 0;
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
	 * Solves conditions combined by logic operators (AND, OR, XOR)
	 * without including braces and brace structured for creating some additional structure.
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	@SuppressWarnings("unchecked")
	private ConditionBase TranslateConditionCombination(long startOffset, long endOffset) throws InvalidOffsetException
	{
		AnnotationSet allMarkupsAS = document.getAnnotations().get(startOffset, endOffset);
		AnnotationSet logicOperatorAS = allMarkupsAS.get("logicOperator");
		ConditionBase resultCondition = null;
		
		LogicOperatorType logicOperatorType = LogicOperatorType.AND;
		
		Iterator<Annotation> logicAnnotationsIterator = logicOperatorAS.iterator();
		ArrayList<Annotation> sortedLogicAnnotations = new ArrayList<Annotation>();
		
		// sort the found logic operators by offset
		while (logicAnnotationsIterator.hasNext())
		{
			sortedLogicAnnotations.add(logicAnnotationsIterator.next());
		}
		
		Collections.sort(sortedLogicAnnotations);
		Iterator<Annotation> sortedLogicAnnotationsIterator = sortedLogicAnnotations.iterator();
		
		// if the statements includes a logic operator (AND, OR, XOR)
		if (sortedLogicAnnotationsIterator.hasNext())
		{
			Annotation logicOperator = sortedLogicAnnotationsIterator.next();

			logicOperatorType = GetLogicOperatorFromString(docContent.getContent(logicOperator.getStartNode().getOffset(), logicOperator.getEndNode().getOffset()).toString());
			
			ConditionBase conditionLeft = TranslateConditionCombination(startOffset, logicOperator.getStartNode().getOffset() - 1); // translate condition left of the found logic operator
			ConditionBase conditionRight = TranslateConditionCombination(logicOperator.getEndNode().getOffset() + 1, endOffset);  //translate condition right of the found logic operator
			
			if (conditionRight instanceof CombinedCondition) // if the right condition is already a combined one...
			{
				if (logicOperatorType.equals(((CombinedCondition) conditionRight).GetLogicOperatorType())) //...the operator is checked and if it is the same logic operator...
				{
					((CombinedCondition) conditionRight).AddCondition(conditionLeft); //...the condition is added
					resultCondition = conditionRight;
					resultCondition.RemoveComment();
				}
				else //...otherwise...
				{
					resultCondition = new CombinedCondition(conditionLeft, logicOperatorType, ""); //...a new condition combination is created with the "new" operator
					((CombinedCondition) resultCondition).AddCondition(conditionRight);
					simpleConditionCombination++;
				}
			}
			else
			{
				resultCondition = new CombinedCondition(conditionLeft, logicOperatorType, "");
				((CombinedCondition) resultCondition).AddCondition(conditionRight);
				simpleConditionCombination++;
			}
			
			resultCondition.AddComment(docContent.getContent(startOffset, endOffset).toString());
			
			return resultCondition;
		}
		else // not a logic operator
		{
			return TranslateConditionOtherThanLogicCombination(startOffset, endOffset); //call the method to distinguish all other conditions
		}
	}
	
	/**
	 * Distinguishes between all other types of conditions except logic combined ones and that ones
	 * that are structured with braces and calls the right method to translate the condition in question
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	private ConditionBase TranslateConditionOtherThanLogicCombination(long startOffset, long endOffset) throws InvalidOffsetException
	{
		AnnotationSet allMarkupsAS = document.getAnnotations().get(startOffset, endOffset);
		ConditionBase result = null;
		
		if (!allMarkupsAS.get("colon").isEmpty()) //if the condition contains colons
		{
			result = TranslateColonList(startOffset, endOffset);
		}
		else if (!allMarkupsAS.get("reference").isEmpty()) //if the condition has reference to a personal pronoun
		{
			result = TranslateReferenceCondition(startOffset, endOffset);	
		}
		else if (!allMarkupsAS.get("lingualLogicOperator").isEmpty()) //if the condition contains linguistic logic operators 
		{
			result = TranslateLinugalLogicTerm(startOffset, endOffset); 
		}
		else if (!allMarkupsAS.get("negation").isEmpty()) //if the condition contains a negation
		{
			result = TranslateNegation(startOffset, endOffset);	
		}
		else if (!allMarkupsAS.get("comparisonOperator").isEmpty()) //if the condition contains comparison operators
		{
			result = TranslateComparison(startOffset, endOffset);				
		}
		else if (!allMarkupsAS.get("range").isEmpty() && !allMarkupsAS.get("Number").isEmpty()) //if the condition contains a range
		{
			result = TranslateRange(startOffset, endOffset);
		}
		else
		{
			result = TranslateMedicalConcept(startOffset, endOffset);	
		}
		
		result.AddComment(docContent.getContent(startOffset, endOffset).toString());
				
		return result;
	}
	
	/**
	 * Translated conditions that are sepperated by a colon
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	@SuppressWarnings("unchecked")
	private ConditionBase TranslateColonList(long startOffset, long endOffset) throws InvalidOffsetException
	{
		ConditionBase result = null;
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet lingualLogicOperatorAS = allMarkupsAS.get("lingualLogicOperator", startOffset, endOffset);
		AnnotationSet informationBraceAS = allMarkupsAS.get("informationBrace", startOffset, endOffset);
		
		if (informationBraceAS.isEmpty()) // if the colons are not in a brace
		{
			AnnotationSet colonAS = allMarkupsAS.get("colon", startOffset, endOffset);
			Iterator<Annotation> colonIterator = colonAS.iterator();
			// sort colons by offset
			ArrayList<Annotation> sortedColon = new ArrayList<Annotation>();
			while (colonIterator.hasNext())
			{
				sortedColon.add(colonIterator.next());
			}
			
			Collections.sort(sortedColon);
			Iterator<Annotation> sortedColonIterator = sortedColon.iterator();
			
			ArrayList<ConditionBase> list = new ArrayList<ConditionBase>();
			long conditionStartOffset = startOffset;
			long conditionEndOffset = endOffset;
			
			while (sortedColonIterator.hasNext()) // for every colon
			{
				Annotation colonPart = sortedColonIterator.next();
				conditionEndOffset = colonPart.getStartNode().getOffset();
				
				list.add(TranslateConditionOtherThanLogicCombination(conditionStartOffset, conditionEndOffset)); //add the condition seperated by the colon to the list of conditions
				
				conditionStartOffset = colonPart.getEndNode().getOffset() + 1;
			}
			
			conditionEndOffset = endOffset;
			
			if (allMarkupsAS.get("lingualLogicOperator", conditionStartOffset, conditionEndOffset).iterator().hasNext()) // if the last condition in the colon list contains a linguistic logic operator (and, or)
			{
				Annotation operator = allMarkupsAS.get("lingualLogicOperator", conditionStartOffset, conditionEndOffset).iterator().next();
				LogicOperatorType logicOperatorType = GetLogicOperatorFromString(docContent.getContent(operator.getStartNode().getOffset(), operator.getEndNode().getOffset()).toString().toUpperCase());
				
				list.add(TranslateConditionOtherThanLogicCombination(operator.getEndNode().getOffset() + 1, conditionEndOffset));
				
				result = new CombinedCondition(list, logicOperatorType, ""); // create a combined condition including the colon seperated conditions
			}
			else
			{
				list.add(TranslateConditionOtherThanLogicCombination(conditionStartOffset, conditionEndOffset));
				result = new CombinedCondition(list, LogicOperatorType.OR, "");
			}
			simpleConditionCombination++;
		}
		else // if the colon seperated conditions are in a brace
		{
			Annotation informationBrace = informationBraceAS.iterator().next();
			long braceStartOffset = informationBrace.getStartNode().getOffset();
			long braceEndOffset = informationBrace.getEndNode().getOffset();
			AnnotationSet colonAS = allMarkupsAS.get("colon", braceStartOffset, braceEndOffset);
			Iterator<Annotation> colonIterator = colonAS.iterator();
			//sort colons by offset
			ArrayList<Annotation> sortedColon = new ArrayList<Annotation>();
			while (colonIterator.hasNext())
			{
				sortedColon.add(colonIterator.next());
			}
			
			Collections.sort(sortedColon);
			Iterator<Annotation> sortedColonIterator = sortedColon.iterator();
			
			long colonPartStartOffset = braceStartOffset;
			long colonPartEndOffset = braceEndOffset;
			
			// brace but no lingual logic operator -> probably a contains-condition (is-member-of)
			if (lingualLogicOperatorAS.isEmpty())
			{
				ArrayList<String> list = new ArrayList<String>();
				String value;
				
				while (sortedColonIterator.hasNext())
				{
					Annotation colonPart = sortedColonIterator.next();
					colonPartEndOffset = colonPart.getStartNode().getOffset();
					list.add(TranslateMetaMapConcept(colonPartStartOffset, colonPartEndOffset));
					colonPartStartOffset = colonPart.getEndNode().getOffset() + 1;
				}
				colonPartEndOffset = braceEndOffset;
				
				list.add(TranslateMetaMapConcept(colonPartStartOffset, colonPartEndOffset));
				
				value = TranslateMetaMapConcept(startOffset, braceStartOffset - 2);
								
				result = new ContainsCondition<String>(value, list, "", "", "");
				isMemberOf++;
			}
			else // if there is a linguistic logic operator
			{
				ArrayList<ConditionBase> list = new ArrayList<ConditionBase>();
				
				while (sortedColonIterator.hasNext())
				{
					Annotation colonPart = sortedColonIterator.next();
					colonPartEndOffset = colonPart.getStartNode().getOffset();
					list.add(TranslateConditionOtherThanLogicCombination(colonPartStartOffset, colonPartEndOffset));
					colonPartStartOffset = colonPart.getEndNode().getOffset() + 1;
				}
				
				colonPartEndOffset = braceEndOffset;
				
				if (allMarkupsAS.get("lingualLogicOperator", colonPartStartOffset, colonPartEndOffset).iterator().hasNext())
				{
					Annotation operator = allMarkupsAS.get("lingualLogicOperator", colonPartStartOffset, colonPartEndOffset).iterator().next();
					LogicOperatorType logicOperatorType = GetLogicOperatorFromString(docContent.getContent(operator.getStartNode().getOffset(), operator.getEndNode().getOffset()).toString().toUpperCase());
					
					list.add(TranslateConditionOtherThanLogicCombination(operator.getEndNode().getOffset() + 1, colonPartEndOffset));
					
					//a combied condition with the found operator is built
					result = new CombinedCondition(list, logicOperatorType, "");
				}
				else
				{
					list.add(TranslateConditionOtherThanLogicCombination(colonPartStartOffset, colonPartEndOffset));
					result = new CombinedCondition(list, LogicOperatorType.OR, "");
				}
				simpleConditionCombination++;
			}
		}
		
		return result;
	}
	
	/**
	 * Translated conditions that include lingual logic conditions (including
	 * and or in a linguistic way instead of an pure logic one)
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	private ConditionBase TranslateLinugalLogicTerm(long startOffset, long endOffset) throws InvalidOffsetException
	{
		ConditionBase result = null;
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet lingualLogicOperatorAS = allMarkupsAS.get("lingualLogicOperator", startOffset, endOffset);
		AnnotationSet informationBraceAS = allMarkupsAS.get("informationBrace", startOffset, endOffset);
		
		Annotation linguatLogicOperator = lingualLogicOperatorAS.iterator().next();
		
		long lingualStartOffset = linguatLogicOperator.getStartNode().getOffset();
		long lingualEndOffset = linguatLogicOperator.getEndNode().getOffset();
		LogicOperatorType logicOperatorType = GetLogicOperatorFromString(docContent.getContent(linguatLogicOperator.getStartNode().getOffset(), linguatLogicOperator.getEndNode().getOffset()).toString().toUpperCase());
		ArrayList<ConditionBase> conditions = new ArrayList<ConditionBase>();
		
		if (!informationBraceAS.isEmpty()) // if the condition is in a brace
		{
			Annotation informationBrace = informationBraceAS.iterator().next();
			
			if (lingualStartOffset > informationBrace.getStartNode().getOffset() && lingualEndOffset < informationBrace.getEndNode().getOffset())
			{
				String leftSide = "";
				String rightSide = "";
				
				if (startOffset < informationBrace.getStartNode().getOffset() - 2)
				{
					leftSide = TranslateMetaMapConcept(startOffset, informationBrace.getStartNode().getOffset() - 2);
				}
				if (informationBrace.getEndNode().getOffset() + 2 < endOffset)
				{
					rightSide = TranslateMetaMapConcept(informationBrace.getEndNode().getOffset() + 2, endOffset);	
				}
				
				//concatinate the phrase before the brace with each of the phrases (split through the linguistic logic word) in the brace and with the phrase after the brace
				conditions.add(new ComparisonCondition(leftSide + " " + TranslateMetaMapConcept(informationBrace.getStartNode().getOffset(), lingualStartOffset - 1) + " " + rightSide, MatchCode.equal, "true", ""));
				conditions.add(new ComparisonCondition(leftSide + " " + TranslateMetaMapConcept(lingualStartOffset + 1, informationBrace.getEndNode().getOffset()) + " " + rightSide, MatchCode.equal, "true", ""));
				comparison++;
				comparison++;
			}
			else // T O    B E      R E F A C T O R E D
			{
				conditions.add(TranslateConditionOtherThanLogicCombination(startOffset, lingualStartOffset - 1));
				conditions.add(TranslateConditionOtherThanLogicCombination(lingualEndOffset + 1, endOffset));
			}
		}
		else // T O    B E      R E F A C T O R E D
		{
			conditions.add(TranslateConditionOtherThanLogicCombination(startOffset, lingualStartOffset - 1));
			conditions.add(TranslateConditionOtherThanLogicCombination(lingualEndOffset + 1, endOffset));
		}
		
		result = new CombinedCondition(conditions, logicOperatorType, "");
		simpleConditionCombination++;
		
		return result;
	}
	
	/**
	 * Translates conditions that include comparisons
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	private ConditionBase TranslateComparison(long startOffset, long endOffset) throws InvalidOffsetException
	{
		ConditionBase result = null;
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet comparisonOperatorAS = allMarkupsAS.get("comparisonOperator", startOffset, endOffset);
		
		long operatorStartOffset = comparisonOperatorAS.firstNode().getOffset();
		long operatorEndOffset = comparisonOperatorAS.lastNode().getOffset();
				
		if (!allMarkupsAS.get("Number", startOffset, endOffset).isEmpty()) //if there is a number contained in the comparison
		{
			Annotation number = allMarkupsAS.get("Number", startOffset, endOffset).iterator().next();
			long numberStartOffset = number.getStartNode().getOffset();
			long numberEndOffset = number.getEndNode().getOffset();
			long fieldStartOffset;
			long fieldEndOffset;
			long unitStartOffset;
			long unitEndOffset;
			
			if (operatorStartOffset < numberStartOffset) //if the number is right of the comparison operator
			{
				fieldStartOffset = startOffset;
				fieldEndOffset = operatorStartOffset - 1;
				unitStartOffset = numberEndOffset;
				unitEndOffset = endOffset;
			}
			else //if the number is left of the comparison operator
			{
				fieldStartOffset = operatorEndOffset + 1;
				fieldEndOffset = endOffset;
				unitStartOffset = numberEndOffset;
				unitEndOffset = operatorStartOffset;
			}
			
			String field = TranslateMetaMapConcept(fieldStartOffset, fieldEndOffset);
			String unit = TranslateMetaMapConcept(unitStartOffset, unitEndOffset);
			
			result = new ComparisonCondition(field, GetMatchCodeFromString(docContent.getContent(operatorStartOffset, operatorEndOffset).toString()), Float.valueOf(docContent.getContent(numberStartOffset, numberEndOffset).toString()), unit, "", "");
		}
		else //if the comparison is between two fields (no number involved)
		{
			long field1StartOffset = startOffset;
			long field1EndOffset = operatorStartOffset - 1;
			long field2StartOffset = operatorStartOffset + 1;
			long field2EndOffset = endOffset;
			
			String field1 = TranslateMetaMapConcept(field1StartOffset, field1EndOffset);
			String field2 = TranslateMetaMapConcept(field2StartOffset, field2EndOffset);
			
			result = new ComparisonCondition(field1, GetMatchCodeFromString(docContent.getContent(operatorStartOffset, operatorEndOffset).toString()), field2, "");
		}
		
		comparison++;
		return result;
	}
	
	/**
	 * Translates conditions that include ranges
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws NumberFormatException
	 * @throws InvalidOffsetException
	 */
	private ConditionBase TranslateRange(long startOffset, long endOffset) throws NumberFormatException, InvalidOffsetException
	{
		ConditionBase result = null;
		String variable = "";
		float lowerBound = 0;
		float upperBound = 0;
		String unit = "";
		String scale = "";
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet rangeOperatorAS = allMarkupsAS.get("range", startOffset, endOffset);
		
		long rangeStartOffset = rangeOperatorAS.firstNode().getOffset();
		long rangeEndOffset = rangeOperatorAS.lastNode().getOffset();
		
		AnnotationSet numbersAS = allMarkupsAS.get("Number", startOffset, rangeStartOffset - 1);
		if (numbersAS.iterator().hasNext()) //first number (lower limit) of the range
		{
			Annotation number = numbersAS.iterator().next();
			lowerBound = Float.valueOf(docContent.getContent(number.getStartNode().getOffset(), number.getEndNode().getOffset()).toString());
			
			variable = TranslateMetaMapConcept(startOffset, number.getStartNode().getOffset() - 1); //variable the range is for
		}
		
		numbersAS = allMarkupsAS.get("Number", rangeEndOffset + 1, endOffset);
		if (numbersAS.iterator().hasNext()) //second (upper limit) number of the range
		{
			Annotation number = numbersAS.iterator().next();
			upperBound = Float.valueOf(docContent.getContent(number.getStartNode().getOffset(), number.getEndNode().getOffset()).toString());
			
			long unitStartOffset = number.getEndNode().getOffset() + 1;
			
			if (unitStartOffset < endOffset)
			{
				unit = TranslateMetaMapConcept(unitStartOffset, endOffset); //unit of the range values
			}
			else
			{
				unit = "";
			}
		}
		
		result = new RangeCondition(variable, unit, scale, lowerBound, upperBound, "");
		isWithinRange++;

		return result;
	}
	
	/**
	 * Translate conditions that include a negation
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	private ConditionBase TranslateNegation(long startOffset, long endOffset) throws InvalidOffsetException
	{
		ConditionBase result = null;
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet negationAS = allMarkupsAS.get("negation", startOffset, endOffset);
		
		Annotation negation = negationAS.iterator().next();
		
		//create a negation
		result = new NotCondition(TranslateConditionOtherThanLogicCombination(negation.getEndNode().getOffset() + 1, endOffset), "");
		simpleConditionNot++;

		return result;
	}
	
	/**
	 * Translate reference conditions. Condition has reference to personal pronoun (mostly patient)
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	@SuppressWarnings("unchecked")
	private ConditionBase TranslateReferenceCondition(long startOffset, long endOffset) throws InvalidOffsetException
	{
		ConditionBase result = null;
		ArrayList<ConditionBase> conditions = new ArrayList<ConditionBase>();
		
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet referenceAS = allMarkupsAS.get("reference", startOffset, endOffset);
		
		Iterator<Annotation> referenceASIterator = referenceAS.iterator();
		ArrayList<Annotation> sortedReferenceAS = new ArrayList<Annotation>();
		
		while (referenceASIterator.hasNext())
		{
			sortedReferenceAS.add(referenceASIterator.next());
		}
		Collections.sort(sortedReferenceAS);
		
		Iterator<Annotation> sortedReferenceASIterator = sortedReferenceAS.iterator();
		long startReferenceOffset = -1;
		
		// translate the found reference conditions...
		while (sortedReferenceASIterator.hasNext())
		{
			Annotation reference = sortedReferenceASIterator.next();
			
			if (startReferenceOffset != -1)
			{
				conditions.add(TranslateConditionOtherThanLogicCombination(startReferenceOffset, reference.getStartNode().getOffset() - 1));
			}
			
			startReferenceOffset = reference.getEndNode().getOffset() + 1;
		}
		
		conditions.add(TranslateConditionOtherThanLogicCombination(startReferenceOffset, endOffset));
		
		if (conditions.size() == 1)
		{
			//...if it is just one return it
			result = conditions.get(0);
			result.RemoveComment();
		}
		else
		{
			//...if there are many (more than one) found conditions combine them together
			result = new CombinedCondition(conditions, LogicOperatorType.AND, "");
			simpleConditionCombination++;
		}
		
		return result;
	}
	
	/**
	 * Translated medical concepts and builds boolean comparisons out of it
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	private ConditionBase TranslateMedicalConcept(long startOffset, long endOffset) throws InvalidOffsetException
	{
		String concept = TranslateMetaMapConcept(startOffset, endOffset);
		
		comparison++;
		return new ComparisonCondition(concept, MatchCode.equal, "true", "");
	}
	
	/**
	 * searches the condition for medical concepts using metamap and tries to
	 * build them together to useful phrases
	 * 
	 * @param startOffset starting offset of the condition part to be translated
	 * @param endOffset ending offset of the condition part to be translated
	 * @return condition object for the condition part that was translated in this method
	 * @throws InvalidOffsetException
	 */
	@SuppressWarnings("unchecked")
	private String TranslateMetaMapConcept(long startOffset, long endOffset) throws InvalidOffsetException
	{
		String field = "";
		AnnotationSet allMarkupsAS = document.getAnnotations();
		AnnotationSet fieldMetaMapAnnotations = allMarkupsAS.get("MetaMap", startOffset, endOffset);
		Iterator<Annotation> fieldMetaMapAnnotationsIterator = fieldMetaMapAnnotations.iterator();
		ArrayList<Annotation> fieldMetaMapAnnotationsSorted = new ArrayList<Annotation>();
		while (fieldMetaMapAnnotationsIterator.hasNext())
		{
			fieldMetaMapAnnotationsSorted.add(fieldMetaMapAnnotationsIterator.next());
		}
		Collections.sort(fieldMetaMapAnnotationsSorted);
		
		Iterator<Annotation> fieldMetaMapAnnotationsSortedIterator = fieldMetaMapAnnotationsSorted.iterator();
		
		while(fieldMetaMapAnnotationsSortedIterator.hasNext())
		{
			Annotation fieldAnnotation = fieldMetaMapAnnotationsSortedIterator.next();
			field = field + docContent.getContent(fieldAnnotation.getStartNode().getOffset(), fieldAnnotation.getEndNode().getOffset()).toString() + " "; //concatenate the metamap concepts of the condition
		}
		if (field.isEmpty()) // if there are no contained metamap concepts, the plain-text is concatinated to get some results
		{
			AnnotationSet fieldTokenAnnotations = allMarkupsAS.get("Token", startOffset, endOffset);
			Iterator<Annotation> fieldTokenAnnotationsIterator = fieldTokenAnnotations.iterator();
			ArrayList<Annotation> fieldTokenAnnotationsSorted = new ArrayList<Annotation>();
			while (fieldTokenAnnotationsIterator.hasNext())
			{
				fieldTokenAnnotationsSorted.add(fieldTokenAnnotationsIterator.next());
			}
			Collections.sort(fieldTokenAnnotationsSorted);
			
			Iterator<Annotation> fieldTokenAnnotationsSortedIterator = fieldTokenAnnotationsSorted.iterator();
			
			while(fieldTokenAnnotationsSortedIterator.hasNext())
			{
				Annotation fieldAnnotation = fieldTokenAnnotationsSortedIterator.next();
				field = field + docContent.getContent(fieldAnnotation.getStartNode().getOffset(), fieldAnnotation.getEndNode().getOffset()).toString() + " ";
			}
		}
		
		if (field.length() > 0)
		{
			field = field.substring(0, field.length() - 1);
		}
		
		return field;
	}
	
	/**
	 * Returns an enumeration of the logic operator type that is hand over as string
	 * 
	 * @param operator operator as String
	 * @return the LogicOperatorType for a condition combination
	 */
	private LogicOperatorType GetLogicOperatorFromString(String operator)
	{
		//operator = operator.toUpperCase(); // only if lingual logic operations are not used
		
		if (operator.equals("AND"))
		{
			return LogicOperatorType.AND;
		}
		else if (operator.equals("OR"))
		{
			return LogicOperatorType.OR;
		}
		else if (operator.equals("XOR"))
		{
			return LogicOperatorType.XOR;
		}
		
		return LogicOperatorType.AND;
	}
	
	/**
	 * Returns an enumeration of the match code that is hand over as string
	 * 
	 * @param matchCode match code as string
	 * @return the match code for a comparison (condition)
	 */
	private MatchCode GetMatchCodeFromString(String matchCode)
	{
		if (matchCode.equals("==") || matchCode.equals("=") || matchCode.equals("equal"))
		{
			return MatchCode.equal;
		}
		else if (matchCode.equals("!=") || matchCode.equals("<>") || matchCode.equals("notEqual"))
		{
			return MatchCode.notEqual;
		}
		else if (matchCode.equals("<") || matchCode.equals("lessThan"))
		{
			return MatchCode.lessThan;
		}
		else if (matchCode.equals("<=") || matchCode.equals("=<") || matchCode.equals("lessOrEqual"))
		{
			return MatchCode.lessOrEqual;
		}
		else if (matchCode.equals(">") || matchCode.equals("greaterThan"))
		{
			return MatchCode.greaterThan;
		}
		else if (matchCode.equals(">=") || matchCode.equals("=>") || matchCode.equals("greaterOrEqual"))
		{
			return MatchCode.greaterOrEqual;
		}
		else
		{
			return MatchCode.equal;
		}
	}
	
	/**
	 * this method pre processes the gate document by rough guessing which conditions could be contained
	 */
	/*private void PreProcessDocument()
	{
		possibleConditionTypes = new ArrayList<ConditionType>();
		AnnotationSet markups = document.getAnnotations();
				
		if (!markups.get("logicOperator").isEmpty())
		{
			possibleConditionTypes.add(ConditionType.CombinedCondition);
		}
		if (!markups.get("negation").isEmpty())
		{
			possibleConditionTypes.add(ConditionType.NotCondition);
		}
		if (!markups.get("openBrace").isEmpty() && !markups.get("closeBrace").isEmpty() && !markups.get("colon").isEmpty() && !markups.get("contains").isEmpty())
		{
			possibleConditionTypes.add(ConditionType.ContainsCondition);
		}
		if (!markups.get("range").isEmpty())
		{
			possibleConditionTypes.add(ConditionType.RangeCondition);
		}
		if (!markups.get("comparisonOperator").isEmpty())
		{
			possibleConditionTypes.add(ConditionType.ComparisonCondition);
		}
	}*/
	
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
	
	private int logicBraceLayerCount;
	private Document document;
	private DocumentContent docContent;
	private String stringContent;	
	//private ArrayList<ConditionType> possibleConditionTypes;
	
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
	
	private static final Logger log = Logger.getLogger("ConditionTranslator.java");
}
