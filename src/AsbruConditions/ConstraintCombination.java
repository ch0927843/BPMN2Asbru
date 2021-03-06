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
public class ConstraintCombination implements ITemporalPattern {

	/**
	 * 
	 * @param label label of the element
	 * @param importance importance attribute of the element
	 * @param type logical operator of the combination
	 * @param patterns patterns that contain the conditions to be combined
	 */
	public ConstraintCombination(String label, float importance, Type type, ArrayList<ITemporalPattern> patterns)
	{
		this(label, importance, Type.AND, patterns, new ArrayList<AnyComment>(), new ArrayList<Explanation>());
	}
	
	/**
	 * 
	 * @param label label of the element
	 * @param importance importance attribute of the element
	 * @param type logical operator of the combination
	 * @param patterns patterns that contain the conditions to be combined
	 * @param comments comments of the element
	 * @param explanations explanations of the element
	 */
	public ConstraintCombination(String label, float importance, Type type, ArrayList<ITemporalPattern> patterns, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations)
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
		if (patterns.isEmpty() || patterns == null)
			throw new IllegalArgumentException("patterns can't be null or emtpy");
		
		this.label = label;
		this.importance = importance;
		this.patterns = patterns;
		this.type = type;
	}

	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		Iterator<Explanation> explanationIterator;
		Explanation explanation;
		Iterator<ITemporalPattern> patternIterator;
		ITemporalPattern pattern;
		
		s = "<constraint-combination label=\"" + label + "\" type=\"" + type.toString().toLowerCase() + "\" importance=\"" + String.valueOf(importance) + "\">";
		
		s = s + commentContainer.printComments();
		
		explanationIterator = explanations.iterator();
		while(explanationIterator.hasNext())
		{
			explanation = explanationIterator.next();
			s = s + explanation.print();
		}
		
		patternIterator = patterns.iterator();
		while(patternIterator.hasNext())
		{
			pattern = patternIterator.next();
			s = s + pattern.print();
		}
		
		s = s + "</constraint-combination>";
		
		return s;
	}
	
	// condition
	private ArrayList<ITemporalPattern> patterns;
	// list of explanations
	private Collection<Explanation> explanations;
	// label of the condition
	private String label;
	// priority of the condition
	private float importance;
	// stores the comments of this object
	private CommentContainer commentContainer;
	// type of the operator
	private Type type;
}
