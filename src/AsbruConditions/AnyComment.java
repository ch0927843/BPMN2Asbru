package AsbruConditions;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class AnyComment implements IPrintable {

	/**
	 * @param comment comment
	 */
	public AnyComment(IActualComment comment)
	{
		Validate.notNull(comment, "comment can't be null");
		
		this.comment = comment;
	}
	
	/**
	 * @param comment comment
	 */
	public void setComment(IActualComment comment)
	{
		Validate.notNull(comment, "comment can't be null");
		
		this.comment = comment;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		
		s = "<any-comment>";
		s = s + comment.print();
		s = s + "</any-comment>";
		
		return s;
	}
	
	// actual comment that contains the content of the comment
	private IActualComment comment;
	//CommentType commentType;	
}
