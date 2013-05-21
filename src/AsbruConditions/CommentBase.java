package AsbruConditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public abstract class CommentBase {

	/**
	 * @param comments list of comments
	 */
	public CommentBase(ArrayList<AnyComment> comments)
	{
		if (!comments.isEmpty())
		{
			this.comments = comments;
		}
		else
		{
			comments = new ArrayList<AnyComment>();
		}
	}
	
	/**
	 * returns the contained comments as valid xml string
	 * @return comments as valid xml string
	 */
	public String printComments()
	{
		String s = "";	
		Iterator<AnyComment> it;
		AnyComment comment;
		
		if (comments != null)
		{
			it = comments.iterator();
			while(it.hasNext())
			{
				comment = it.next();
				s = s + comment.print();
			}
			return s;
		}
		else
		{
			return "";
		}
	}
	
	// list of comments
	private Collection<AnyComment> comments;
}
