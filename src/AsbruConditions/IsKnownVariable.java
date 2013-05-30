package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class IsKnownVariable extends AbstractSimpleCondition {

	/**
	 * @param name name of the variable
	 */
	public IsKnownVariable(String name)
	{
		this(name, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param name name of the variable
	 * @param comments comments of the element
	 */
	public IsKnownVariable(String name, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		
		Validate.notNull(name, "name can't be null");
		
		this.name = name;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<is-known-variable name=\"" + name + "\">";
		s = s + commentContainer.printComments();
		s = s + "</is-known-variable>";
		
		return s;
	}
	
	// name of the variable
	private String name;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
