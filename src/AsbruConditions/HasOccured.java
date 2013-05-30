package AsbruConditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class HasOccured extends AbstractSimpleCondition {

	/**
	 * @param planStateTransition
	 */
	public HasOccured(PlanStateTransition planStateTransition)
	{
		this(planStateTransition, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param planStateTransition
	 * @param comments comments of the element
	 */
	public HasOccured(PlanStateTransition planStateTransition, ArrayList<AnyComment> comments)
	{
		commentContainer = new CommentContainer(comments);
		
		Validate.notNull(planStateTransition, "planStateTransition can't be null");
		
		this.planStateTransition = planStateTransition;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<has-occurred>";
		s = s + commentContainer.printComments();
		s = s + planStateTransition.print();
		s = s + "</has-occurred>";
		
		return s;
	}
	
	// instance of the plan state transition
	private PlanStateTransition planStateTransition;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
