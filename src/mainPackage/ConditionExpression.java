package mainPackage;

import org.apache.commons.lang.Validate;

/**
 * container for a plain-text phrase of a CPG that includes conditions and the corresponding ID of the condition
 * @author Christian Hinterer
 */
public class ConditionExpression 
{

	/**
	 * @param id ID to identify the condition
	 * @param expression plain-text phrase of a CPG that includes conditions
	 */
	public ConditionExpression(String id, String expression)
	{
		Validate.notNull(id, "id can't be null");
		Validate.notNull(expression, "expression can't be null");
		
		this.id = id;
		this.expression = expression;
	}
	
	/**
	 * @return ID to identify the condition
	 */
	public String Id()
	{
		return id;
	}
	
	/**
	 * @return plain-text phrase that contains conditions
	 */
	public String Expression()
	{
		return expression;
	}
	
	// ID to identify the condition
	private String id;
	// plain-text phrase of a CPG that includes conditions
	private String expression;
}
