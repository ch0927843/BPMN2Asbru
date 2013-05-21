package AsbruConditions;


public class FilterPreconditionFactory {

	public static FilterPrecondition CreateFilterPrecondition(IAbstractSimpleCondition abstractSimpleCondition, String id)
	{
		SimpleCondition simpleCondition = new SimpleCondition("", 0, abstractSimpleCondition);
		
		FilterPrecondition filterPrecondition = new FilterPrecondition(simpleCondition, id);
		
		return filterPrecondition;
	}
	
}
