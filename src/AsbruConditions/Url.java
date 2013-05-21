package AsbruConditions;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Url implements IActualComment {

	/**
	 * @param text the url as text string
	 */
	public Url(String text)
	{
		this.text = text;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		
		s = "<url text=\"" + text + "\"" + " />";
		
		return s;
	}
	
	// the url as text string
	private String text;
}
