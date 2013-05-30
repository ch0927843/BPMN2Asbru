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
public class ConstraintNot implements ITemporalPattern {

	/**
	 * 
	 * @param label label of the element
	 * @param importance importance attribute of the element
	 * @param pattern pattern that contains the actual condition
	 */
	public ConstraintNot(String label, float importance, ITemporalPattern pattern)
	{
		this(label, importance, pattern, new ArrayList<AnyComment>(), new ArrayList<Explanation>());
	}
	
	/**
	 * 
	 * @param label label of the element
	 * @param importance importance attribute of the element
	 * @param pattern pattern that contains the actual condition
	 * @param comments comments of the element
	 * @param explanations explanations of the element
	 */
	public ConstraintNot(String label, float importance, ITemporalPattern pattern, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations)
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
		Validate.notNull(pattern, "pattern can't be null");
		
		this.label = label;
		this.importance = importance;
		this.pattern = pattern;
	}

	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		Iterator<Explanation> it;
		Explanation explanation;
		
		s = "<constraint-not label=\"" + label + "\" importance=\"" + String.valueOf(importance) + "\">";
		
		s = s + commentContainer.printComments();
		
		it = explanations.iterator();
		while(it.hasNext())
		{
			explanation = it.next();
			s = s + explanation.print();
		}
		
		s = s + pattern.print();
		s = s + "</constraint-not>";
		
		return s;
	}
	
	// condition
	private ITemporalPattern pattern;
	// list of explanations
	private Collection<Explanation> explanations;
	// label of the condition
	private String label;
	// priority of the condition
	private float importance;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
