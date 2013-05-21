package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class NotYetDefined extends CommentBase implements IActualComment {

	public NotYetDefined()
	{
		this(new ArrayList<AnyComment>());
	}
	
	/**
	 * @param comments comments of the element
	 */
	public NotYetDefined(ArrayList<AnyComment> comments)
	{
		super(comments);
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		
		s = "<not-yet-defined>";
		s = s + printComments();
		s = s + "</not-yet-defined>";
		
		return s;
	}
}
