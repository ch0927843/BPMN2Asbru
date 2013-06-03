package Conditions;

import java.util.ArrayList;
import org.apache.commons.lang.Validate;
import AsbruConditions.AnyComment;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IsUnknownVariable;

/**
 * condition to check if a variable is unknown
 * @author Christian Hinterer
 */
public class VariableUnknownCondition extends ConditionBase {

	/**
	 * @param name name of the variable to check
	 * @param id ID to identify the condition
	 */
	public VariableUnknownCondition(String name, String id)
	{
		super(id);
		
		Validate.notNull(name, "name can't be null");
		
		this.name = name;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.IsUnknownVariable
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
		
		return FilterPreconditionFactory.CreateFilterPreconditionFromAbstractSimpleCondition(new IsUnknownVariable(name, asbruComments), id);
	}
	
	// name of the variable to check
	private String name;
}
