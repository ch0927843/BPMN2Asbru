package AsbruConditions;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Comment implements IActualComment {

	/**
	 * @param text content of the comment
	 */
	public Comment(String text)
	{
		this.text = text;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		
		s = "<comment text=\"" + text + "\"" + " />";
		
		return s;
	}
	
	// content of the comment
	private String text;
}
