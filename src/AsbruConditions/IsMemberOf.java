package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class IsMemberOf extends CommentBase implements IAbstractSimpleCondition {

	/**
	 * @param element element to find in list
	 * @param listOrSetRef list of elements to search in
	 */
	public IsMemberOf(Element element, ListOrSetRef listOrSetRef)
	{
		this(element, listOrSetRef, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param element element to find in list
	 * @param listOrSetRef list of elements to search in
	 * @param comments comments of the element
	 */
	public IsMemberOf(Element element, ListOrSetRef listOrSetRef, ArrayList<AnyComment> comments)
	{
		super(comments);
		
		Validate.notNull(element, "element can't be null");
		Validate.notNull(listOrSetRef, "listOrSetRef can't be null");
		
		this.element = element;
		this.listOrSetRef = listOrSetRef;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<is-member-of>";
		s = s + printComments();
		s = s + element.print();
		s = s + listOrSetRef.print();
		s = s + "</is-member-of>";
		
		return s;
	}
	
	// element to find in list
	private Element element;
	// list of elements to search in
	private ListOrSetRef listOrSetRef;
}
