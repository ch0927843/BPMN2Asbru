package AsbruConditions;

import java.util.ArrayList;
import java.util.UUID;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class StaticPlanPointer extends CommentBase implements IPrintable {

	/**
	 * @param planName name of the plan
	 * @param label plan label
	 */
	public StaticPlanPointer(UUID planName, String label)
	{
		this(planName, label, new ArrayList<AnyComment>(), null);
	}
	
	/**
	 * @param planName name of the plan
	 * @param label plan label
	 * @param comments comments of the element
	 * @param invokingPlan concerned plan
	 */
	public StaticPlanPointer(UUID planName, String label, ArrayList<AnyComment> comments, InvokingPlan invokingPlan)
	{
		super(comments);
		this.planName = planName;
		this.label = label;
		this.invokingPlan = invokingPlan;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<static-plan-pointer plan-name=\"" + planName.toString() + "\" label=" + label + "\">";
		s = s + printComments();
		if (invokingPlan != null)
		{
			s = s + invokingPlan.print();
		}
		s = s + "</static-plan-pointer>";		
		
		return s;
	}
	
	// name of the plan
	private UUID planName;
	// plan label
	private String label;
	// concerned plan
	private InvokingPlan invokingPlan;
}
