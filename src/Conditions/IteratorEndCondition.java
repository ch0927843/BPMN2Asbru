package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IsAtEnd;

/**
 * condition for iterator at end
 * @author Christian Hinterer
 */
public class IteratorEndCondition extends ConditionBase {

	/**
	 * @param iterator name of iterator
	 * @param id ID to identify the condition
	 */
	public IteratorEndCondition(String iterator, String id)
	{
		super(id);
		
		Validate.notNull(iterator, "iterator can't be null");
		
		this.iterator = iterator;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.IsAtEnd
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition Convert()
	{
		ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
		
		for(String comment: comments)
		{
			asbruComments.add(new AnyComment(new Comment(comment)));
		}
		
		return FilterPreconditionFactory.CreateFilterPreconditionFromAbstractSimpleCondition(new IsAtEnd(iterator, asbruComments), id);
	}
	
	// name of iterator
	private String iterator;
}
