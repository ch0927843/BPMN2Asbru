package Conditions;

import java.util.ArrayList;
import org.apache.commons.lang.Validate;
import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IsKnownVariable;

/**
 * condition to check if a variable is known
 * @author Christian Hinterer
 */
public class VariableKnownCondition extends ConditionBase {

	/**
	 * @param name name of the variable to check
	 * @param id ID to identify the condition
	 */
	public VariableKnownCondition(String name, String id)
	{
		super(id);
		
		Validate.notNull(name, "name can't be null");
		
		this.name = name;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.IsKnownVariable
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
		
		return FilterPreconditionFactory.CreateFilterPreconditionFromAbstractSimpleCondition(new IsKnownVariable(name, asbruComments), id);
	}
	
	// name of the variable to check
	private String name;
}
