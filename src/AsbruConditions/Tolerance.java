package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Tolerance extends CommentBase implements IPrintable {

	/**
	 * @param constantExpression the expression that defines the tolerance
	 */
	public Tolerance(ConstantExpression constantExpression)
	{
		this(constantExpression, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param constantExpression the expression that defines the tolerance
	 * @param comments comments of the element
	 */
	public Tolerance(ConstantExpression constantExpression, ArrayList<AnyComment> comments)
	{
		super(comments);
		this.constantExpression = constantExpression;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<tolerance>";
		s = s + printComments();
		s = s + constantExpression.print();
		s = s + "</tolerance>";
		
		return s;
	}
	
	// the expression that defines the tolerance
	private ConstantExpression constantExpression;
}
