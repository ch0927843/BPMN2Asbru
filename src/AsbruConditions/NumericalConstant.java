package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class NumericalConstant extends CommentBase implements IAsbruExpressionChild, IConstantExpressionChild {

	/**
	 * @param value the value of the constant
	 */
	public NumericalConstant(float value)
	{
		this(value, "", "");
	}
	
	/**
	 * @param value the value of the constant
	 * @param unit the unit of the constant
	 * @param scale the scale of the constant
	 */
	public NumericalConstant(float value, String unit, String scale)
	{
		this(value, unit, scale, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param value the value of the constant
	 * @param unit the unit of the constant
	 * @param scale the scale of the constant
	 * @param comments comments of the element
	 */
	public NumericalConstant(float value, String unit, String scale, ArrayList<AnyComment> comments)
	{
		super(comments);
		this.value = value;
		this.unit = unit;
		this.scale = scale;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<numerical-constant value=\"" + Float.toString(value) + "\" unit=\"" + unit + "\" scale=\"" + scale + "\">";
		s = s + printComments();
		s = s + "</numerical-constant>";
		
		return s;
	}
	
	// the value of the constant
	private float value;
	// the unit of the constant
	private String unit;
	// the scale of the constant
	private String scale;
}
