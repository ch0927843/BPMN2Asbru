package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class ArgumentRef implements IAsbruExpressionChild {

	/**
	 * @param name name of the reference
	 */
	public ArgumentRef(String name)
	{
		this(name, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param name name of the reference
	 * @param comments comments of the element
	 */
	public ArgumentRef(String name, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		this.name = name;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<argument-ref name=\"" + name + "\">";
		s = s + commentContainer.printComments();
		s = s + "</argument-ref>";
		
		return s;
	}
	
	// name of the reference
	private String name;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
