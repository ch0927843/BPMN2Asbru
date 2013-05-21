package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class ConstantRef extends CommentBase implements IAsbruExpressionChild, IConstantExpressionChild {

	/**
	 * @param name name of the reference
	 */
	public ConstantRef(String name)
	{
		this(name, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param name name of the reference
	 * @param comments comments of the element
	 */
	public ConstantRef(String name, ArrayList<AnyComment> comments)
	{
		super(comments);
		this.name = name;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<constant-ref name=\"" + name + "\">";
		s = s + printComments();
		s = s + "</constant-ref>";
		
		return s;
	}
	
	// name of the reference
	private String name;
}
