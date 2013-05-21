package AsbruConditions;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Bibref implements IActualComment {

	/**
	 * @param key
	 * @param page
	 */
	public Bibref(String key, String page)
	{
		this.key = key;
		this.page = page;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		
		s = "<bibref key=\"" + key + "\"" + " page=\"" + page + "\" />";
		
		return s;
	}
	
	private String key;
	private String page;
}
