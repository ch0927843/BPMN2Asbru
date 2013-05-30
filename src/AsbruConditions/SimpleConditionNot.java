package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class SimpleConditionNot extends AbstractSimpleCondition {

	/**
	 * @param condition condition to negate
	 */
	public SimpleConditionNot(AbstractSimpleCondition condition)
	{
		this(condition, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param condition condition to negate
	 * @param comments comments of the element
	 */
	public SimpleConditionNot(AbstractSimpleCondition condition, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		
		Validate.notNull(condition, "condition can't be null");
		
		this.condition = condition;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		
		s = "<simple-condition-not>";
		s = s + commentContainer.printComments();
		s = s + condition.print();
		s = s + "</simple-condition-not>";
		
		return s;
	}
	
	// condition to negate
	private AbstractSimpleCondition condition;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
