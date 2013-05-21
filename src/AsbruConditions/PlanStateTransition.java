package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class PlanStateTransition extends CommentBase implements IAsbruExpressionChild {

	/**
	 * @param anyPlanPointer
	 * @param state
	 * @param instanceType
	 */
	public PlanStateTransition(AnyPlanPointer anyPlanPointer, State state, InstanceType instanceType)
	{
		this(anyPlanPointer, new ArrayList<AnyComment>(), state, Direction.enter, instanceType, 1);
	}
	
	/**
	 * @param anyPlanPointer
	 * @param state
	 * @param direction
	 * @param instanceType
	 * @param instanceNumber
	 */
	public PlanStateTransition(AnyPlanPointer anyPlanPointer, State state, Direction direction, InstanceType instanceType, int instanceNumber)
	{
		this(anyPlanPointer, new ArrayList<AnyComment>(), state, direction, instanceType, instanceNumber);
	}
	
	/**
	 * @param anyPlanPointer
	 * @param comments comments of the element
	 * @param state
	 * @param direction
	 * @param instanceType
	 * @param instanceNumber
	 */
	public PlanStateTransition(AnyPlanPointer anyPlanPointer, ArrayList<AnyComment> comments, State state, Direction direction, InstanceType instanceType, int instanceNumber)
	{
		super(comments);
		this.anyPlanPointer = anyPlanPointer;
		this.state = state;
		this.direction = direction;
		this.instanceType = instanceType;
		this.instanceNumber = instanceNumber;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<plan-state-transition state=\"" + state.toString() + "\" direction=\"" + direction.toString() + "\"";
		s = s + " instance-type=\"" + instanceType.toString() + "\" instance-number=\"" + Integer.toString(instanceNumber) + "\">";
		s = s + printComments();
		s = s + anyPlanPointer.print();
		s = s + "</plan-state-transition>";
		
		return s;
	}
	
	private AnyPlanPointer anyPlanPointer;
	private State state;
	private Direction direction;
	private InstanceType instanceType;
	private int instanceNumber;
}
