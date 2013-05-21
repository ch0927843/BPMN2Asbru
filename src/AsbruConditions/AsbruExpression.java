package AsbruConditions;

//usually just "Expression", but this would be ambigious with the expression of the gate namespace
/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * <p>
 * usually just "Expression", but this would be ambigious with the expression of the gate namespace
 * <p>
 * @author Christian Hinterer
 */
public class AsbruExpression implements IPrintable {

	/**
	 * @param expressionChild
	 */
	public AsbruExpression(IAsbruExpressionChild expressionChild)
	{
		this.expressionChild = expressionChild;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<expression>";
		s = s + expressionChild.print();
		s = s + "</expression>";
		
		return s;
	}
	
	private IAsbruExpressionChild expressionChild;
}
