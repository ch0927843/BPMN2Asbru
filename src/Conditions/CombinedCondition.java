package Conditions;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.Validate;

import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.ConstraintCombination;
import AsbruConditions.Explanation;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.ITemporalPattern;
import AsbruConditions.Type;
import Conditions.Converter.ConditionConverter;

/**
 * condition to combine many conditions with logic operators
 * @author Christian Hinterer
 */
public class CombinedCondition extends ConditionBase {

	/**
	 * @param condition first condition to be combined
	 * @param type type of combination operator (AND, OR, XOR)
	 * @param id ID to identify the condition
	 */
	public CombinedCondition(ConditionBase condition, LogicOperatorType type, String id)
	{
		super(id);
		
		Validate.notNull(condition, "condition can't be null");
		Validate.notNull(type, "type can't be null");
		
		conditions = new ArrayList<ConditionBase>();
		conditions.add(condition);
		this.type = type;
	}
	
	/**
	 * @param conditions list of conditions that are combined
	 * @param type type of combination operator (AND, OR, XOR)
	 * @param id ID to identify the condition
	 */
	public CombinedCondition(Collection<ConditionBase> conditions, LogicOperatorType type, String id)
	{
		super(id);
		
		if (conditions.isEmpty() || conditions == null)
			throw new IllegalArgumentException("conditions can't be null or emtpy");
		Validate.notNull(type, "type can't be null");
		
		this.conditions = conditions;
		this.type = type;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.SimpleConditionCombination
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition convert()
	{
		ArrayList<ITemporalPattern> temporalPatterns = new ArrayList<ITemporalPattern>(); 
		
		for(ConditionBase condition: conditions)
		{
			FilterPrecondition filterPrecondition = condition.convert();
			ITemporalPattern temporalPattern = filterPrecondition.getPattern();

			temporalPatterns.add(temporalPattern);
		}
		
		Type asbruType = ConditionConverter.convert(type);
		ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
		
		for(String comment: comments)
		{
			asbruComments.add(new AnyComment(new Comment(comment)));
		}
		
		//return FilterPreconditionFactory.CreateFilterPreconditionFromAbstractSimpleCondition(new SimpleConditionCombination(asbruConditions, asbruType, asbruComments), id);
		
		ConstraintCombination constrainCombination = new ConstraintCombination("null", 0, asbruType, temporalPatterns, asbruComments, new ArrayList<Explanation>());

		return new FilterPrecondition(constrainCombination, id);
	}
	
	/**
	 * adds a condition to the list of conditions that are combined
	 * @param condition condition to add
	 */
	public void addCondition(ConditionBase condition)
	{
		conditions.add(condition);
	}
	
	/**
	 * returns the LogicOperatorType
	 * 
	 * @return LogicOperatorType
	 */
	public LogicOperatorType getLogicOperatorType()
	{
		return type;
	}
	
	// list of conditions to combine
	private Collection<ConditionBase> conditions;
	// type of combination operator (logic AND OR XOR)
	private LogicOperatorType type;
}
