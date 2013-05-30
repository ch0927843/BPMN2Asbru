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
public class SimpleCondition implements ITemporalPattern {

	/**
	 * @param label label of the condition
	 * @param importance priority
	 * @param condition the condition itself
	 */
	public SimpleCondition(String label, float importance, AbstractSimpleCondition condition)
	{
		this(label, importance, condition, new ArrayList<AnyComment>(), new ArrayList<Explanation>());
	}
	
	/**
	 * @param label label of the condition
	 * @param importance priority
	 * @param condition the condition itself
	 * @param comments comments of the element
	 * @param explanations additional explanations
	 */
	public SimpleCondition(String label, float importance, AbstractSimpleCondition condition, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations)
	{
		commentContainer = new CommentContainer(comments);
		
		if (!explanations.isEmpty())
		{
			this.explanations = explanations;
		}
		else
		{
			this.explanations = new ArrayList<Explanation>();
		}
		
		Validate.notNull(label, "label can't be null");
		Validate.notNull(importance, "importance can't be null");
		Validate.notNull(condition, "condition can't be null");
		
		if (!label.isEmpty())
		{
			this.label = label;
		}
		else
		{
			this.label = "null";
		}
		
		this.importance = importance;
		this.condition = condition;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		Iterator<Explanation> it;
		Explanation explanation;
		
		s = "<simple-condition label=\"" + label + "\" importance=\"" + String.valueOf(importance) + "\">";
		
		s = s + commentContainer.printComments();
		
		it = explanations.iterator();
		while(it.hasNext())
		{
			explanation = it.next();
			s = s + explanation.print();
		}
		
		s = s + condition.print();
		s = s + "</simple-condition>";
		
		return s;
	}
	
	/**
	 * return the condition wrapped in this class
	 * @return the actual condition
	 */
	public AbstractSimpleCondition GetCondition()
	{
		return condition;
	}
	
	// condition
	private AbstractSimpleCondition condition;
	// list of explanations
	private Collection<Explanation> explanations;
	// label of the condition
	private String label;
	// priority of the condition
	private float importance;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
