package mainPackage;

/**
 * enumeration containing the different condition types that are known by Asbru
 * 
 * @author Christian Hinterer
 *
 */

public enum ConditionType {
	CombinedCondition, ComparisonCondition, ContainsCondition, IteratorStartCondition, IteratorEndCondition, 
	NotCondition, OccuranceCondition, RangeCondition, VariableKnownCondition, VariableUnknownCondition
}
