package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class IsAtStart extends AbstractSimpleCondition {

	/**
	 * @param iterator name of the iterator
	 */
	public IsAtStart(String iterator)
	{
		this(iterator, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param iterator name of the iterator
	 * @param comments comments of the element
	 */
	public IsAtStart(String iterator, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		
		Validate.notNull(iterator, "iterator can't be null");
		
		this.iterator = iterator;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<is-at-start iterator=\"" + iterator + "\">";
		s = s + commentContainer.printComments();
		s = s + "</is-at-start>";
		
		return s;
	}
	
	// name of the iterator
	private String iterator;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
