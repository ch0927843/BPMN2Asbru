package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class Comparison extends AbstractSimpleCondition {

	/**
	 * @param comparisonType operator of the comparison
	 * @param left left side of the comparison
	 * @param right right side of the comparison
	 */
	public Comparison(ComparisonType comparisonType, LeftHandSide left, RightHandSide right)
	{
		this(comparisonType, left, right, new ArrayList<AnyComment>(), null);
	}
	
	/**
	 * @param comparisonType operator of the comparison
	 * @param left left side of the comparison
	 * @param right right side of the comparison
	 * @param comments comments of the element
	 */
	public Comparison(ComparisonType comparisonType, LeftHandSide left, RightHandSide right, ArrayList<AnyComment> comments)
	{
		this(comparisonType, left, right, comments, null);
	}
	
	/**
	 * @param comparisonType operator of the comparison
	 * @param left left side of the comparison
	 * @param right right side of the comparison
	 * @param comments comments of the element
	 * @param tolerance of the comparison
	 */
	public Comparison(ComparisonType comparisonType, LeftHandSide left, RightHandSide right, ArrayList<AnyComment> comments, Tolerance tolerance)
	{
		commentContainer = new CommentContainer(comments);
		
		Validate.notNull(comparisonType, "comparisonType can't be null");
		Validate.notNull(left, "left can't be null");
		Validate.notNull(right, "right can't be null");
		
		this.comparisonType = comparisonType;
		this.left = left;
		this.right = right;
		this.tolerance = tolerance;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<comparison type=\"" + printComparisonType() + "\" >";
		s = s + commentContainer.printComments();
		s = s + left.print();
		s = s + right.print();
		if (tolerance != null)
		{
			s = s + tolerance.print();
		}
		s = s + "</comparison>";
		
		return s;
	}
	
	/**
	 * 
	 * @param comparisonType comparison type
	 * @return a String that contains the comparison type in Asbru language
	 */
	private String printComparisonType()
	{
		switch(comparisonType)
		{
			case lessThan:
				return "less-than";
			case lessOrEqual:
				return "less-or-equal";
			case greaterThan:
				return "greater-than";
			case greaterOrEqual:
				return "greater-or-equal";
			case equal:
				return "equal";
			case notEqual:
				return "not-equal";
			default:
				return "equal";
		}
	}
	
	// operator of the comparison
	private ComparisonType comparisonType;
	// left side of the comparison
	private LeftHandSide left;
	// right side of the comparison
	private RightHandSide right;
	// tolerance of the comparison
	private Tolerance tolerance;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
