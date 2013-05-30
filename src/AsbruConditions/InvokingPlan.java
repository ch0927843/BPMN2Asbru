package AsbruConditions;

import java.util.ArrayList;
import java.util.UUID;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class InvokingPlan implements IPrintable {

	/**
	 * @param planName
	 */
	public InvokingPlan(UUID planName)
	{
		this(planName, new ArrayList<AnyComment>(), null);
	}
	
	/**
	 * @param planName
	 * @param comments comments of the element
	 * @param invokingPlan
	 */
	public InvokingPlan(UUID planName, ArrayList<AnyComment> comments, InvokingPlan invokingPlan)
	{
		commentContainer = new CommentContainer(comments);
		this.planName = planName;
		this.invokingPlan = invokingPlan;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<invoking-plan plan-name=\"" + planName.toString() + "\">";
		s = s + commentContainer.printComments();
		if (invokingPlan != null)
		{
			s = s + invokingPlan.print();
		}
		s = s + "</invoking-plan>";
		
		return s;
	}
	
	private UUID planName;
	private InvokingPlan invokingPlan;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
