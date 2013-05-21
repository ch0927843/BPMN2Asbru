package AsbruConditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

public class ConstraintCombination extends CommentBase implements ITemporalPattern {

	public ConstraintCombination(String label, float importance, Type type, ArrayList<ITemporalPattern> patterns)
	{
		this(label, importance, Type.AND, patterns, new ArrayList<AnyComment>(), new ArrayList<Explanation>());
	}
	
	public ConstraintCombination(String label, float importance, Type type, ArrayList<ITemporalPattern> patterns, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations)
	{
		super(comments);
		
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
	}

	public String print()
	{
		String s = "";
		Iterator<Explanation> explanationIterator;
		Explanation explanation;
		Iterator<ITemporalPattern> patternIterator;
		ITemporalPattern pattern;
		
		s = "<constraint-combination label=\"" + label + "\" importance=\"" + String.valueOf(importance) + "\">";
		
		s = s + printComments();
		
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
}
