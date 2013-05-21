package AsbruConditions;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class ListOrSetRef implements IPrintable {

	/**
	 * @param listOrSetRefChild child of the reference
	 */
	public ListOrSetRef(IListOrSetRefChild listOrSetRefChild)
	{
		this.listOrSetRefChild = listOrSetRefChild;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<list-or-set-ref>";
		s = s + listOrSetRefChild.print();
		s = s + "</list-or-set-ref>";
		
		return s;
	}
	
	// child of the reference
	private IListOrSetRefChild listOrSetRefChild;
}
