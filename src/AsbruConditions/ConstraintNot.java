package AsbruConditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

public class ConstraintNot extends CommentBase implements ITemporalPattern {

	  public ConstraintNot(String label, float importance, ITemporalPattern pattern)
		{
			this(label, importance, pattern, new ArrayList<AnyComment>(), new ArrayList<Explanation>());
		}
		
		public ConstraintNot(String label, float importance, ITemporalPattern pattern, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations)
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
			Validate.notNull(pattern, "pattern can't be null");
			
			this.label = label;
			this.importance = importance;
			this.pattern = pattern;
		}

		public String print()
		{
			String s = "";
			Iterator<Explanation> it;
			Explanation explanation;
			
			s = "<constraint-not label=\"" + label + "\" importance=\"" + String.valueOf(importance) + "\">";
			
			s = s + printComments();
			
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
}
