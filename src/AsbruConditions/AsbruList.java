package AsbruConditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//usually just "List", but this would be ambigious with the List of the java.util namespace
/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * <p>
 * usually just "List", but this would be ambigious with the List of the java.util namespace
 * <p>
 * @author Christian Hinterer
 */
public class AsbruList implements IListOrSetRefChild {

	public AsbruList()
	{
		this(new ArrayList<AnyComment>(), new ArrayList<AsbruExpression>());
	}
	
	/**
	 * @param comments comments of the element
	 * @param expressions list of expression
	 */
	public AsbruList(ArrayList<AnyComment> comments, ArrayList<AsbruExpression> expressions)
	{
		commentContainer = new CommentContainer(comments);
		this.expressions = expressions;
	}
	
	/**
	 * adds an expression to the list of expressions
	 * @param expression expression to add in list
	 */
	public void Add(AsbruExpression expression)
	{
		expressions.add(expression);
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		Iterator<AsbruExpression> it;
		AsbruExpression expression;
		
		s = "<list>";
		s = s + commentContainer.printComments();
		
		it = expressions.iterator();
		while(it.hasNext())
		{
			expression = it.next();
			s = s + expression.print();
		}
		
		s = s + "</list>";
		
		return s;
	}
	
	// list of expressions
	private Collection<AsbruExpression> expressions;
	// stores the comments of this object
	private CommentContainer commentContainer;
}
