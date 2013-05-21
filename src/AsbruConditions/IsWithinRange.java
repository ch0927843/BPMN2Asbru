package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class IsWithinRange extends CommentBase implements IAbstractSimpleCondition {

	/**
	 * @param expression expression that contains the value for the range check
	 * @param valueRange contains the information of the range for the range check
	 */
	public IsWithinRange(AsbruExpression expression, ValueRange valueRange)
	{
		this(expression, valueRange, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param expression expression that contains the value for the range check
	 * @param valueRange contains the information of the range for the range check
	 * @param comments comments of the element
	 */
	public IsWithinRange(AsbruExpression expression, ValueRange valueRange, ArrayList<AnyComment> comments)
	{
		super(comments);
		
		Validate.notNull(expression, "expression can't be null");
		Validate.notNull(valueRange, "valueRange can't be null");
		
		this.expression = expression;
		this.valueRange = valueRange;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<is-within-range>";
		s = s + printComments();
		s = s + expression.print();
		s = s + valueRange.print();
		s = s + "</is-within-range>";
		
		return s;
	}
	
	// expression that contains the value for the range check
	private AsbruExpression expression;
	// contains the information of the range for the range check
	private ValueRange valueRange;
}
