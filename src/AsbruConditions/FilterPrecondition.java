package AsbruConditions;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

public class FilterPrecondition extends CommentBase {
	
	public FilterPrecondition(ITemporalPattern pattern, String id)
	{
		this(pattern, false, false, new ArrayList<AnyComment>(), new ArrayList<Explanation>(), id);
	}
	
	public FilterPrecondition(ITemporalPattern pattern, boolean overridable, boolean confirmationRequired, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations, String id)
	{
		super(comments);
		
		Validate.notNull(pattern, "pattern can't be null");
	
		this.pattern = pattern;
		this.overridable = overridable;
		this.confirmationRequired = confirmationRequired;
		this.explanations = explanations;
		this.id = id;
	}
	
	public String print()
	{
		String s = "";
		Iterator<Explanation> it;
		Explanation explanation;
		String sOverridable;
		String sConfirmationRequired;
		
		//quick fix; need to refactor
		if (overridable == true)
		{
			sOverridable = "yes";
		}
		else
		{
			sOverridable = "no";
		}
		
		if (confirmationRequired == true)
		{
			sConfirmationRequired = "yes";
		}
		else
		{
			sConfirmationRequired = "no";
		}
		
		s =  "<filter-precondition overridable=\"" + sOverridable + "\" confirmation-required=\"" + sConfirmationRequired + "\" ID=\"" + id + "\">";
		if (pattern != null)
		{
			s = s + printComments();
			it = explanations.iterator();
			while(it.hasNext())
			{
				explanation = it.next();
				s = s + explanation.print();
			}
			s = s + pattern.print();
		}
		s = s + "</filter-precondition>";
		
		return s;
	}
	
	public ITemporalPattern GetPattern()
	{
		return pattern;
	}
	
	private ITemporalPattern pattern;
	
	private boolean overridable;
	
	private boolean confirmationRequired;
	
	private ArrayList<Explanation> explanations;
	
	private String id;
}
