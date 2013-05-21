package Conditions;

import java.util.ArrayList;
import org.apache.commons.lang.Validate;

import AsbruConditions.FilterPrecondition;

/**
 * base class for any type of conditions
 * @author Christian Hinterer
 */
public abstract class ConditionBase implements IConvertable {

	/**
	 * @param id ID to identify the condition
	 */
	public ConditionBase(String id)
	{
		Validate.notNull(id, "id can't be null");
		
		comments = new ArrayList<String>();
		this.id = id;
	}

	public abstract FilterPrecondition Convert();
	
	/**
	 * adds a comment to the condition
	 * @param comment
	 */
	public void AddComment(String comment)
	{
		comment = comment.replaceAll("<", "&lt;");
		comment = comment.replaceAll(">", "&gt;");
		comments.add(comment);
	}
	
	/**
	 * removes the last comment of the list
	 */
	public void RemoveComment()
	{
		if (!comments.isEmpty())
		{
			comments.remove(comments.size() - 1);
		}
	}
	
	/**
	 * sets the id for the condition
	 * @param id ID
	 */
	public void SetID(String id)
	{
		this.id = id;
	}
	
	// list of comments
	protected ArrayList<String> comments;
	
	// ID to identify the condition
	protected String id;
}
