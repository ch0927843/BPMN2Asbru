package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class LowerBound implements IPrintable {

	/**
	 * @param expression the expression that defines the bound
	 */
	public LowerBound(AsbruExpression expression)
	{
		this(expression, true, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param expression the expression that defines the bound
	 * @param includeLimit defines the usage of the bound limit
	 * @param comments the comments of the element
	 */
	public LowerBound(AsbruExpression expression, boolean includeLimit, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		this.expression = expression;
		this.includeLimit = includeLimit;
	}

	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		String sIncludeLimit = "";
		
		if (includeLimit == true)
		{
			sIncludeLimit = "yes";
		}
		else
		{
			sIncludeLimit = "no";
		}
		
		s = "<lower-bound include-limit=\"" + sIncludeLimit + "\" >";
		s = s + commentContainer.printComments();
		s = s + expression.print();
		s = s + "</lower-bound>";
		
		return s;
	}
	
	// expression that defines the bound
	private AsbruExpression expression;
	// defines the usgage of the bound limit
	private boolean includeLimit;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
