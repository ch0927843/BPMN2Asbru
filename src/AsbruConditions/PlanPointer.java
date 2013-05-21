package AsbruConditions;

import java.util.ArrayList;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class PlanPointer extends CommentBase implements IAnyPlanPointerChild {

	/**
	 * @param staticPlanPointer
	 */
	public PlanPointer(StaticPlanPointer staticPlanPointer)
	{
		this(InstanceType.first, 1, staticPlanPointer, new ArrayList<AnyComment>());
	}
	
	/**
	 * @param instanceType
	 * @param instanceNumber
	 * @param staticPlanPointer
	 * @param comments comments of the element
	 */
	public PlanPointer(InstanceType instanceType, int instanceNumber, StaticPlanPointer staticPlanPointer, ArrayList<AnyComment> comments)
	{
		super(comments);
		this.instanceType = instanceType;
		this.instanceNumber = instanceNumber;
		this.staticPlanPointer = staticPlanPointer;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print() 
	{
		String s = "";
		
		s = "<plan-pointer instance-type=\"" + instanceType.toString() + "\" instance-number=\"" + Integer.toString(instanceNumber) + "\">";
		s = s + printComments();
		s = s + staticPlanPointer.print();
		s = s + "</plan-pointer>";
		
		return s;
	}

	private InstanceType instanceType;
	private int instanceNumber;
	private StaticPlanPointer staticPlanPointer;
}
