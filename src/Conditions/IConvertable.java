package Conditions;

import AsbruConditions.FilterPrecondition;

/**
 * interface for all conditions that are convertable to Asbru-conditions
 * @author Christian Hinterer
 */
public interface IConvertable {

	public FilterPrecondition Convert();
}
