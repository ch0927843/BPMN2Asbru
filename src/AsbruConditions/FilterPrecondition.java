package AsbruConditions;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian
 */
public class FilterPrecondition {
	
	/**
	 * 
	 * @param pattern pattern that contains the actual condition
	 * @param id id of the condition
	 */
	public FilterPrecondition(ITemporalPattern pattern, String id)
	{
		this(pattern, false, false, new ArrayList<AnyComment>(), new ArrayList<Explanation>(), id);
	}
	
	/**
	 * 
	 * @param pattern pattern that contains the actual condition
	 * @param overridable overridable attribute
	 * @param confirmationRequired confirmationRequired attribute
	 * @param comments comments of the element
	 * @param explanations explanatinos of the element
	 * @param id id of the condition
	 */
	public FilterPrecondition(ITemporalPattern pattern, boolean overridable, boolean confirmationRequired, ArrayList<AnyComment> comments, ArrayList<Explanation> explanations, String id)
	{
		commentContainer = new CommentContainer(comments);
		
		Validate.notNull(pattern, "pattern can't be null");
	
		this.pattern = pattern;
		this.overridable = overridable;
		this.confirmationRequired = confirmationRequired;
		this.explanations = explanations;
		this.id = id;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		Iterator<Explanation> it;
		Explanation explanation;
		String sOverridable;
		String sConfirmationRequired;
		
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
			s = s + commentContainer.printComments();
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
	
	/**
	 * returns the condition that is wrapped in this class
	 *  
	 * @return pattern that contains the actual condition
	 */
	public ITemporalPattern getPattern()
	{
		return pattern;
	}
	
	// instance of a pattern that contains the actual condition
	private ITemporalPattern pattern;
	// overridable attribute
	private boolean overridable;
	// confirmationRequired attribute
	private boolean confirmationRequired;
	// list of explanations
	private ArrayList<Explanation> explanations;
	// id of the condition
	private String id;
	// stores the comments of the condition
	private CommentContainer commentContainer;
}
