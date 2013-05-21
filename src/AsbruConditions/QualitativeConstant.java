package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class QualitativeConstant extends CommentBase implements IAsbruExpressionChild, IConstantExpressionChild {

	/**
	 * @param value the value of the constant
	 */
	public QualitativeConstant(String value)
	{
		this(value, new ArrayList<AnyComment>());
	}

	/**
	 * @param value the value of the constant
	 * @param comments comments of the element
	 */
	public QualitativeConstant(String value, ArrayList<AnyComment> comments)
	{
		super(comments);
		this.value = value;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<qualitative-constant value=\"" + value + "\">";
		s = s + printComments();
		s = s + "</qualitative-constant>";
		
		return s;
	}
	
	// the value of the constant
	private String value;
}
