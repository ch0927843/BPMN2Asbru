package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.*;
import Conditions.Converter.ConditionConverter;

/**
 * condition for comparisons
 * @author Christian Hinterer
 */
public class ComparisonCondition extends ConditionBase {
	
	/**	
	 * @param field field to compare
	 * @param matchCode comparison operator
	 * @param value string value to compare
	 * @param id ID to identify the condition
	 */
	public ComparisonCondition(String field, MatchCode matchCode, String value, String id)
	{
		super(id);
		
		Validate.notNull(field, "field can't be null");
		Validate.notNull(matchCode, "matchCode can't be null");
		Validate.notNull(value, "value can't be null");
		
		this.field = field;
		this.sValue = value;
		this.matchCode = matchCode;
		this.comparisonMemberType = ComparisonMemberType.fieldStringvalue;
	}
	
	/**
	 * @param field field to compare
	 * @param matchCode comparison operator
	 * @param value float value to compare
	 * @param id ID to identify the condition
	 */
	public ComparisonCondition(String field, MatchCode matchCode, float value, String id)
	{
		this(field, matchCode, value, "", "", id);
	}
	
	/**
	 * @param field field to compare
	 * @param matchCode comparison operator
	 * @param value float value to compare
	 * @param unit unit value
	 * @param scale scale of value
	 * @param id ID to identify the condition
	 */
	public ComparisonCondition(String field, MatchCode matchCode, float value, String unit, String scale, String id)
	{
		super(id);
		
		Validate.notNull(field, "field can't be null");
		Validate.notNull(matchCode, "matchCode can't be null");
		Validate.notNull(value, "value can't be null");
		
		this.field = field;
		this.fValue = value;
		this.matchCode = matchCode;
		this.unit = unit;
		this.scale = scale;
		this.comparisonMemberType = ComparisonMemberType.fieldNumericvalue;
	}
	
	/**
	 * @param field field one to compare
	 * @param field2 field two to compare
	 * @param matchCode comparison operator
	 * @param id ID to identify the condition
	 */
	public ComparisonCondition(String field, String field2, MatchCode matchCode, String id)
	{
		super(id);
		
		Validate.notNull(field, "field can't be null");
		Validate.notNull(field2, "field2 can't be null");
		Validate.notNull(matchCode, "matchCode can't be null");
		
		this.field = field;
		this.field2 = field2;
		this.matchCode = matchCode;
		this.comparisonMemberType = ComparisonMemberType.fieldField;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.Comparison
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition Convert()
	{
		ComparisonType comparisonType = ConditionConverter.Convert(matchCode);
		LeftHandSide left;
		RightHandSide right;
		AsbruExpression expLeft;
		AsbruExpression expRight;
		
		if (comparisonMemberType == ComparisonMemberType.fieldNumericvalue)
		{
			expLeft = new AsbruExpression(new ParameterRef(field));
			expRight = new AsbruExpression(new NumericalConstant(fValue, unit, scale));
			left = new LeftHandSide(expLeft);
			right = new RightHandSide(expRight);
		}
		else if (comparisonMemberType == ComparisonMemberType.fieldStringvalue)
		{
			expLeft = new AsbruExpression(new ParameterRef(field));
			if (sValue.equals("true") || sValue.equals("false"))
			{
				expRight = new AsbruExpression(new QualitativeConstant(sValue));
			}
			else
			{
				expRight = new AsbruExpression(new StringConstant(sValue));
			}
			left = new LeftHandSide(expLeft);
			right = new RightHandSide(expRight);
			
		}
		else if (comparisonMemberType == ComparisonMemberType.fieldField)
		{
			expLeft = new AsbruExpression(new ParameterRef(field));
			expRight = new AsbruExpression(new ParameterRef(field2));
			left = new LeftHandSide(expLeft);
			right = new RightHandSide(expRight);
		}
		else
		{
			left = new LeftHandSide(null);
			right = new RightHandSide(null);
		}
		
		if (comments.isEmpty())
		{
			return FilterPreconditionFactory.CreateFilterPrecondition(new Comparison(comparisonType, left, right), id);
		}
		else
		{
			ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
			
			for(String comment: comments)
			{
				asbruComments.add(new AnyComment(new Comment(comment)));
			}
			
			return FilterPreconditionFactory.CreateFilterPrecondition(new Comparison(comparisonType, left, right, asbruComments), id);
		}
	}
	
	// field to compare
	private String field;
	// field to compare
	private String field2;
	// string value to compare
	private String sValue;
	// floate value to compare
	private float fValue;
	// unit of the combined field
	private String unit;
	// scale of the combined field
	private String scale;
	// combination-match-code (equal, notequal,...)
	private MatchCode matchCode;
	// type of comparison (field with string value, field with float value, field with field) 
	private ComparisonMemberType comparisonMemberType;
}
