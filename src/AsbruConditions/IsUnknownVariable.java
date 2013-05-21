package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class IsUnknownVariable extends CommentBase implements IAbstractSimpleCondition {

	/**
	 * @param name name of the variable
	 */
	public IsUnknownVariable(String name)
	{
		this(name, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param name name of the variable
	 * @param comments comments of the element
	 */
	public IsUnknownVariable(String name, ArrayList<AnyComment> comments)
	{
		super(comments);
		
		Validate.notNull(name, "name can't be null");
		
		this.name = name;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<is-unknown-variable name=\"" + name + "\">";
		s = s + printComments();
		s = s + "</is-unknown-variable>";
		
		return s;
	}
	
	// name of the variable
	private String name;
}
