package Conditions;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import AsbruConditions.AnyComment;
import AsbruConditions.AnyPlanPointer;
import AsbruConditions.Comment;
import AsbruConditions.FilterPrecondition;
import AsbruConditions.FilterPreconditionFactory;
import AsbruConditions.HasOccured;
import AsbruConditions.PlanStateTransition;
import AsbruConditions.Self;
import Conditions.Converter.ConditionConverter;

/**
 * condition to check the occurance of an instance
 * @author Christian Hinterer
 */
public class OccuranceCondition extends ConditionBase {

	/**
	 * @param stateType state type of the condition
	 * @param instanceType instance type of the condition
	 * @param id ID to identify the condition
	 */
	public OccuranceCondition(StateType stateType, InstanceTyp instanceType, String id)
	{
		this(stateType, instanceType, DirectionType.enter, 1, id);
	}
	
	/**
	 * @param stateType state type of the condition
	 * @param instanceType instance type of the condition
	 * @param directionType direction type of the condition
	 * @param instanceNumber instance number of the condition 
	 * @param id ID to identify the condition
	 */
	public OccuranceCondition(StateType stateType, InstanceTyp instanceType, DirectionType directionType, int instanceNumber, String id)
	{
		super(id);
		
		Validate.notNull(stateType, "stateType can't be null");
		Validate.notNull(instanceType, "instanceType can't be null");
		
		this.stateType = stateType;
		this.instanceType = instanceType;
		this.directionType = directionType;
		this.instanceNumber = instanceNumber;
	}
	
	/**
	 * Converts the condition into a condition of the Asbru-language (AsbruConditions.AsbstractSimpleCondition),
	 * that contains a AsbruConditions.HasOccured
	 * 
	 * @return the corresponding Asbru-condition
	 */
	public FilterPrecondition convert()
	{
		// at the moment just self in implemented, but plan-pointer can be implemented on demand
		Self self = new Self();
		
		AnyPlanPointer pointer = new AnyPlanPointer(self);
		PlanStateTransition planStateTransition = new PlanStateTransition(pointer, ConditionConverter.convert(stateType), 
				ConditionConverter.convert(directionType), ConditionConverter.convert(instanceType), instanceNumber);
	
		ArrayList<AnyComment> asbruComments = new ArrayList<AnyComment>();
		
		for(String comment: comments)
		{
			asbruComments.add(new AnyComment(new Comment(comment)));
		}
		
		return FilterPreconditionFactory.createFilterPreconditionFromAbstractSimpleCondition(new HasOccured(planStateTransition, asbruComments), id);
	}
	
	// state type of the condition
	private StateType stateType;
	// instance type of the condition
	private InstanceTyp instanceType;
	// direction type of the condition
	private DirectionType directionType;
	// instance number of the condition
	private int instanceNumber;
}
