package AsbruConditions;

/**
 * The FilterPreconditionFactory wrapps any type of condition in a object instance of FilterPrecondition
 * @author Christian
 */
public class FilterPreconditionFactory {

	/**
	 * 
	 * @param abstractSimpleCondition the condition to be wrapped
	 * @param id the id of the condition
	 * @return the abstractSimpleCondition wrapped in a FilterPreondition
	 */
	public static FilterPrecondition createFilterPreconditionFromAbstractSimpleCondition(AbstractSimpleCondition abstractSimpleCondition, String id)
	{
		SimpleCondition simpleCondition = new SimpleCondition("null", 0, abstractSimpleCondition);
		
		FilterPrecondition filterPrecondition = new FilterPrecondition(simpleCondition, id);
		
		return filterPrecondition;
	}
}
