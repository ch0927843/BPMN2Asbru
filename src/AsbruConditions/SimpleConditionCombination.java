package AsbruConditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class SimpleConditionCombination extends CommentBase implements IAbstractSimpleCondition {

	/**
	 * @param conditions list of conditions to combine
	 * @param type logical operator of the combination
	 */
	public SimpleConditionCombination(ArrayList<IAbstractSimpleCondition> conditions, Type type)
	{
		this(conditions, type, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param conditions list of conditions to combine
	 * @param type logical operator of the combination
	 * @param comments comments of the element
	 */
	public SimpleConditionCombination(ArrayList<IAbstractSimpleCondition> conditions, Type type, ArrayList<AnyComment> comments)
	{
		super(comments);
		
		if (conditions.isEmpty() || conditions == null)
			throw new IllegalArgumentException("conditions can't be null or emtpy");
		Validate.notNull(type, "type can't be null");
		
		this.conditions = conditions;
		this.type = type;
	}
	
	/**
	 * adds a condition the the combination
	 * @param condition condition to add
	 */
	public void addCondition(IAbstractSimpleCondition condition)
	{
		if (condition != null)
		{
			conditions.add(condition);
		}
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s;
		Iterator<IAbstractSimpleCondition> it;
		IAbstractSimpleCondition condition;
		
		s = "<simple-condition-combination type=\"" + type.toString() + "\">";
		s = s + printComments();
		it = conditions.iterator();
		while(it.hasNext())
		{
			condition = it.next();
			s = s + condition.print();
		}
		s = s + "</simple-condition-combination>";
		
		return s;
	}
	
	// list of conditions to combine with a logical operator
	private Collection<IAbstractSimpleCondition> conditions;
	// type of the operator
	private Type type;
}
