package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IAbstractSimpleCondition;
import AsbruConditions.SimpleCondition;
import AsbruConditions.SimpleConditionNot;

/**
 * models the negation of a condition
 * @author Christian Hinterer
 */
public class NotCondition extends ConditionBase {

	/**
	 * @param condition condition to be negated
	 * @param id ID to identify the condition
	 */
	public NotCondition(ConditionBase condition, String id)
	{
		super(id);
		
		Validate.notNull(condition, "condition can't be null");
		
		this.condition = condition;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.SimpleConditionNot
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition Convert()
	{
		FilterPrecondition filterPrecondition = condition.Convert();
		IAbstractSimpleCondition abstractCondition = ((SimpleCondition)filterPrecondition.GetPattern()).GetCondition();
		
		if (comments.isEmpty())
		{
			return FilterPreconditionFactory.CreateFilterPrecondition(new SimpleConditionNot(abstractCondition), id);
		}
		else
		{
			ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
			
			for(String comment: comments)
			{
				asbruComments.add(new AnyComment(new Comment(comment)));
			}
			
			return FilterPreconditionFactory.CreateFilterPrecondition(new SimpleConditionNot(abstractCondition, asbruComments), id);
		}
	}
	
	// condition to be negated
	private ConditionBase condition;
}
