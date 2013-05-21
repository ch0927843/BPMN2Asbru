package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.AnyComment;
import AsbruConditions.AsbruExpression;
import AsbruConditions.AsbruList;
import AsbruConditions.Comment;
import AsbruConditions.Element;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.IsMemberOf;
import AsbruConditions.ListOrSetRef;
import AsbruConditions.NumericalConstant;
import AsbruConditions.StringConstant;

/**
 * condition to model if a value or a field is contained in a list of values or fields
 * @author Christian Hinterer
 */
public class ContainsCondition<T> extends ConditionBase {

	/**
	 * @param value value to find
	 * @param valueList list of values
	 * @param id ID to identify the condition
	 */
	public ContainsCondition(T value, ArrayList<T> valueList, String id)
	{
		this(value, valueList, "", "", id);
	}
	
	public ContainsCondition(T value, ArrayList<T> valueList, String unit, String scale, String id)
	{
		super(id);
		
		Validate.notNull(value, "value can't be null");
		if (valueList.isEmpty() || valueList == null)
			throw new IllegalArgumentException("valueList can't be null or emtpy");
		
		this.value = value;
		this.valueList = valueList;
		this.unit = unit;
		this.scale = scale;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.IsMemberOf
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition Convert()
	{
		AsbruExpression expression;
		AsbruList asbruList = new AsbruList();
		
		if (value instanceof Float)
		{
			NumericalConstant constant = new NumericalConstant(Float.parseFloat(value.toString()), unit, scale);
			expression = new AsbruExpression(constant);
			
			for(T entry: valueList)
			{
				NumericalConstant numConst = new NumericalConstant(Float.parseFloat(entry.toString()), unit, scale);
				AsbruExpression exp = new AsbruExpression(numConst);
				asbruList.Add(exp);
			}
		}
		else if (value instanceof String)
		{
			StringConstant constant = new StringConstant(value.toString());
			expression = new AsbruExpression(constant);
			
			for(T entry: valueList)
			{
				StringConstant stringConst = new StringConstant(entry.toString());
				AsbruExpression exp = new AsbruExpression(stringConst);
				asbruList.Add(exp);
			}
		}
		else
		{
			throw new UnsupportedOperationException();
		}
		
		Element element = new Element(expression); 
		ListOrSetRef listOrSetRef = new ListOrSetRef(asbruList);
		
		if (comments.isEmpty())
		{
			return FilterPreconditionFactory.CreateFilterPrecondition(new IsMemberOf(element, listOrSetRef), id);
		}
		else
		{
			ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
			
			for(String comment: comments)
			{
				asbruComments.add(new AnyComment(new Comment(comment)));
			}
			
			return FilterPreconditionFactory.CreateFilterPrecondition(new IsMemberOf(element, listOrSetRef, asbruComments), id);
		}
	}
	
	// value to find in list
	private T value;
	// list of values
	private ArrayList<T> valueList;
	// unit of the value
	private String unit;
	// scale of the value
	private String scale;
}