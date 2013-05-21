package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.AnyComment;
import AsbruConditions.AsbruExpression;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IsWithinRange;
import AsbruConditions.LowerBound;
import AsbruConditions.NumericalConstant;
import AsbruConditions.StringConstant;
import AsbruConditions.UpperBound;
import AsbruConditions.ValueRange;

/**
 * condition to check if a value is between to other values
 * @author Christian Hinterer
 */

public class RangeCondition extends ConditionBase {

	/**
	 * @param variable variable to check (if it is between the bounds)
	 * @param lowerBound lower bound of comparison
	 * @param upperBound upper bound of comparison
	 * @param id ID to identify the condition
	 */
	public RangeCondition(String variable, float lowerBound, float upperBound, String id)
	{
		this(variable, null, null, lowerBound, upperBound, id);
	}
	
	/**
	 * @param variable variable to check (if it is between the bounds)
	 * @param unit unit of the value
	 * @param scale scale of the value
	 * @param lowerBound lower bound of comparison
	 * @param upperBound upper bound of comparison
	 * @param id ID to identify the condition
	 */
	public RangeCondition(String variable, String unit, String scale, float lowerBound, float upperBound, String id)
	{
		super(id);
		
		Validate.notNull(variable, "value can't be null");
		Validate.notNull(lowerBound, "lowerBound can't be null");
		Validate.notNull(upperBound, "upperBound can't be null");
		
		this.variable = variable;
		this.unit = unit;
		this.scale = scale;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.IsWithinRange
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition Convert()
	{
		AsbruExpression expVal = new AsbruExpression(new StringConstant(variable));
		AsbruExpression expLower = new AsbruExpression(new NumericalConstant(lowerBound, unit, scale));
		AsbruExpression expUpper = new AsbruExpression(new NumericalConstant(upperBound, unit, scale));
		LowerBound lower = new LowerBound(expLower);
		UpperBound upper = new UpperBound(expUpper);
		ValueRange range = new ValueRange(lower, upper);
		
		if (comments.isEmpty())
		{
			return FilterPreconditionFactory.CreateFilterPrecondition(new IsWithinRange(expVal, range), id);
		}
		else
		{
			ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
			
			for(String comment: comments)
			{
				asbruComments.add(new AnyComment(new Comment(comment)));
			}
			
			return FilterPreconditionFactory.CreateFilterPrecondition(new IsWithinRange(expVal, range, asbruComments), id);
		}
	}
	
	// variable to check (if it is between the bounds)
	private String variable;
	// unit of the value
	private String unit;
	// scale of the value
	private String scale;
	// lower bound of comparison
	private float lowerBound;
	// upper bound of comparison
	private float upperBound;
}
