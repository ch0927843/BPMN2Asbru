package AsbruConditions;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class ConstantExpression implements IPrintable {

	/**
	 * @param constantExpression
	 */
	public ConstantExpression(IConstantExpressionChild constantExpression)
	{
		this.constantExpression = constantExpression;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<constant-expression>";
		s = s + constantExpression.print();
		s = s + "</constant-expression>";
		
		return s;
	}
	
	private IConstantExpressionChild constantExpression;
}
