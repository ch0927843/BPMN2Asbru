package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Element extends CommentBase implements IPrintable {

	/**
	 * @param expression
	 */
	public Element(AsbruExpression expression)
	{
		this(expression, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param expression
	 * @param comments comments of the element
	 */
	public Element(AsbruExpression expression, ArrayList<AnyComment> comments)
	{
		super(comments);
		this.expression = expression;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<element>";
		s = s + printComments();
		s = s + expression.print();
		s = s + "</element>";
		
		return s;
	}
	
	private AsbruExpression expression;
}
