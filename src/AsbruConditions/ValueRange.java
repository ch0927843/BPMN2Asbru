package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class ValueRange extends CommentBase implements IPrintable {

	/**
	 * empty value range
	 */
	public ValueRange()
	{
		this(new ArrayList<AnyComment>(), null, null);
	}
	
	/**
	 * @param lowerBound lower bound of the value range
	 * @param upperBound upper bound of the value range
	 */
	public ValueRange(LowerBound lowerBound, UpperBound upperBound)
	{
		this(new ArrayList<AnyComment>(), lowerBound, upperBound);
	}
	
	/**
	 * @param comments comments of the element
	 * @param lowerBound lower bound of the value range
	 * @param upperBound upper bound of the value range
	 */
	public ValueRange(ArrayList<AnyComment> comments, LowerBound lowerBound, UpperBound upperBound)
	{
		super(comments);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<ValueRange>";
		s = s + printComments();
		s = s + lowerBound.print();
		s = s + upperBound.print();
		s = s + "</ValueRange>";
		
		return s;
	}
	
	// lower bound of the value range
	private LowerBound lowerBound;
	// upper bound of the value range
	private UpperBound upperBound;
}
