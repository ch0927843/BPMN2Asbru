package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class LeftHandSide implements IPrintable {

	/**
	 * @param expression expression to define a value for the comparison's right side
	 */
	public LeftHandSide(AsbruExpression expression)
	{
		this(expression, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param expression expression to define a value for the comparison's right side
	 * @param comments comments of the element
	 */
	public LeftHandSide(AsbruExpression expression, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		this.expression = expression;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<left-hand-side>";
		s = s + commentContainer.printComments();
		s = s + expression.print();
		s = s + "</left-hand-side>";
		
		return s;
	}
	
	// expression to define a value for the comparison's right side
	private AsbruExpression expression;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
