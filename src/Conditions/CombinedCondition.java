package Conditions;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.Validate;
import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IAbstractSimpleCondition;
import AsbruConditions.SimpleCondition;
import AsbruConditions.SimpleConditionCombination;
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
	public FilterPrecondition Convert()
	{
		ArrayList<IAbstractSimpleCondition> asbruConditions = new ArrayList<IAbstractSimpleCondition>(); 
		
		for(ConditionBase condition: conditions)
		{
			FilterPrecondition filterPrecondition = condition.Convert();
			IAbstractSimpleCondition abstractCondition = ((SimpleCondition)filterPrecondition.GetPattern()).GetCondition();

			asbruConditions.add(abstractCondition);
		}
		
		Type asbruType = ConditionConverter.Convert(type);
		
		if (comments.isEmpty())
		{
			return FilterPreconditionFactory.CreateFilterPrecondition(new SimpleConditionCombination(asbruConditions, asbruType), id);	
		}
		else
		{
			ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
			
			for(String comment: comments)
			{
				asbruComments.add(new AnyComment(new Comment(comment)));
			}
			
			return FilterPreconditionFactory.CreateFilterPrecondition(new SimpleConditionCombination(asbruConditions, asbruType, asbruComments), id);
		}
	}
	
	/**
	 * adds a condition to the list of conditions that are combined
	 * @param condition condition to add
	 */
	public void AddCondition(ConditionBase condition)
	{
		conditions.add(condition);
	}
	
	/**
	 * returns the LogicOperatorType
	 * 
	 * @return LogicOperatorType
	 */
	public LogicOperatorType GetLogicOperatorType()
	{
		return type;
	}
	
	// list of conditions to combine
	private Collection<ConditionBase> conditions;
	// type of combination operator (logic AND OR XOR)
	private LogicOperatorType type;
}
