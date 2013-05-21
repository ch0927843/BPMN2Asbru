package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Explanation extends CommentBase implements IPrintable {

	public Explanation()
	{
		this("", new ArrayList<AnyComment>(), null);
	}
	
	/**
	 * @param text explanation short text
	 * @param comments comments of the element
	 * @param expression expression that contains the content of the Explanation
	 */
	public Explanation(String text, ArrayList<AnyComment> comments, AsbruExpression expression)
	{
		super(comments);
		this.expression = expression;
		this.text = text;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<explanation text=\"" + text + "\">";
		s = s + printComments();
		if (expression != null)
		{
			s = s + expression.print(); 
		}
		s = s + "</explanation>";
		
		return s;
	}
	
	// expression that contains the content of the explanation
	private AsbruExpression expression;
	// explanation short text
	private String text;
}
