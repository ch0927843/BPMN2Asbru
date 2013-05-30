package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class ParameterRef implements IAsbruExpressionChild {

	/**
	 * @param name the parameter reference
	 */
	public ParameterRef(String name)
	{
		this(name, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param name the parameter reference
	 * @param comments comments of the element
	 */
	public ParameterRef(String name, ArrayList<AnyComment> comments)
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
		
		s = "<parameter-ref name=\"" + name + "\">";
		s = s + commentContainer.printComments();
		s = s + "</parameter-ref>";
		
		return s;
	}
	
	// the parameter reference
	private String name;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
