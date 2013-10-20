package Conditions.Converter;

import java.util.ArrayList;
import java.util.Collection;

import AsbruConditions.*;
import Conditions.*;

/**
 * description: this class implements a Converter, converting objects of classes of the package
 * "Conditions" (that represent "real life conditions") into objects of classes of the package
 * "AsbruConditions" (that represent the conditions in exactly that format that is
 * defined at the dtd of the Asgard/Asbru
 * <p>
 * Instead of using a Converter that distinguishes the different classes, i implemented the
 * method to convert the condition directly in the different condition classes;
 * this method makes it more difficult to change the Converter-Implementation, but in this
 * scenario using this pattern is more efficient
 *
 * author: Christian Hinterer
 */
public final class ConditionConverter {

	private ConditionConverter()
	{}

	/**
	 * @param condition condition to convert (into Asbru-language)
	 * @return converted condition IN Asbru-language
	 */
	public static FilterPrecondition convert(ConditionBase condition)
	{
		return condition.convert();
	}
	
	/**
	 * @param conditions list of conditions to convert (into Asbru-language)
	 * @return list of conditions IN Asbru-language
	 */
	public static Collection<FilterPrecondition> convert(Collection<ConditionBase> conditions)
	{
		Collection<FilterPrecondition> asbruConditions = new ArrayList<FilterPrecondition>();
		
		for (ConditionBase condition: conditions)
		{
			if (condition != null)
			{
				asbruConditions.add(condition.convert());
			}
		}
		
		return asbruConditions;
	}
	
	/**
	 * @param matchCode matchCode for comparisons
	 * @return matchCode IN Asbru-language
	 */
	public static ComparisonType convert(MatchCode matchCode)
	{
		switch(matchCode)
		{
			case equal:
				return ComparisonType.equal;
			case notEqual:
				return ComparisonType.notEqual;
			case lessThan:
				return ComparisonType.lessThan;
			case lessOrEqual:
				return ComparisonType.lessOrEqual;
			case greaterThan:
				return ComparisonType.greaterThan;
			case greaterOrEqual:
				return ComparisonType.greaterOrEqual;
			default:
				return ComparisonType.equal;
		}
	}
	
	/**
	 * @param type logic operator type to convert (into Asbru-language)
	 * @return logic operator type IN Asbru-language
	 */
	public static Type convert(LogicOperatorType type)
	{
		switch(type)
		{
			case AND:
				return Type.AND;
			case OR:
				return Type.OR;
			case XOR:
				return Type.XOR;
			default:
				return Type.AND;
		}
	}
	
	/**
	 * @param type state type to convert (into Asbru-language)
	 * @return state type IN Asbru-language
	 */
	public static State convert(StateType type)
	{
		switch(type)
		{
			case considered:
				return State.considered;
			case possible:
				return State.possible;
			case ready:
				return State.ready;
			case activated:
				return State.activated;
			case suspended:
				return State.suspended;
			case aborted:
				return State.aborted;
			case completed:
				return State.completed;
			case selected:
				return State.selected;
			case executed:
				return State.executed;
			default:
				return State.considered;
		}
	}
	
	/**
	 * @param type direction type to convert (into Asbru-language)
	 * @return direction type IN Asbru-language
	 */
	public static Direction convert(DirectionType type)
	{
		switch(type)
		{
			case leave:
				return Direction.leave;
			case enter:
				return Direction.enter;
			default:
				return Direction.enter;
		}
	}
	
	/**
	 * @param type instnace type to convert (into Asbru-language)
	 * @return instance type IN Asbru-language
	 */
	public static InstanceType convert(InstanceTyp type)
	{
		switch(type)
		{
			case first:
				return InstanceType.first;
			case last:
				return InstanceType.last;
			default:
				return InstanceType.first;
		}
	}
	
	
	
	// instead of calling the Convert-methods of the different classes, the distinction of the actual types could be made inside the converter
	// another possibility would be the use of generics, but this is not necessary in this case
	/*private static Comparison Convert(ComparisonCondition condition)
	{
		return null;
	}
	
	private static SimpleConditionCombination Convert(CombinedCondition condition)
	{
		return null;
	}
	
	private static IsMemberOf Convert(ContainsCondition condition)
	{
		return null;
	}
	
	private static SimpleConditionNot Convert(NotCondition condition)
	{
		return null;
	}
	
	private static IsWithinRange Convert(RangeCondition condition)
	{
		return null;
	}
	
	private static HasOccured Convert(OccuranceCondition condition)
	{
		return null;
	}
	
	private static IsKnownVariable Convert(VariableKnownCondition condition)
	{
		return null;
	}
	
	private static IsUnknownVariable Convert(VariableUnknownCondition condition)
	{
		return null;
	}
	
	private static IsAtStart Convert(IteratorStartCondition condition)
	{
		return null;
	}
	
	private static IsAtEnd Convert(IteratorEndCondition condition)
	{
		return null;
	}*/
}
