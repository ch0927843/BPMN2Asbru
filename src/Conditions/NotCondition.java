package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.AbstractSimpleCondition;
import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.ConstraintNot;
import AsbruConditions.Explanation;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.ITemporalPattern;
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
	public FilterPrecondition convert()
	{
		FilterPrecondition filterPrecondition = condition.convert();
		
		ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
		for(String comment: comments)
		{
			asbruComments.add(new AnyComment(new Comment(comment)));
		}
		
		ITemporalPattern temporalPatterns = filterPrecondition.getPattern();
		
		if (temporalPatterns instanceof SimpleCondition)
		{
			AbstractSimpleCondition abstractCondition = ((SimpleCondition)temporalPatterns).getCondition();
			
			return FilterPreconditionFactory.createFilterPreconditionFromAbstractSimpleCondition(new SimpleConditionNot(abstractCondition, asbruComments), id);
		}
		
		else
		{
			ConstraintNot constraintNot = new ConstraintNot("null", 0, temporalPatterns, asbruComments, new ArrayList<Explanation>());
			
			return new FilterPrecondition(constraintNot, id);
		}
	}
	
	// condition to be negated
	private ConditionBase condition;
}
