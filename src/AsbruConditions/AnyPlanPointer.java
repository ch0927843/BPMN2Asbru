package AsbruConditions;

/**
 * this is a container class for the corresponding element of the Asbru-language (defined in the Asgaard/Asbru)
 * for more details read COMMENT_DESCRIPTION.TXT of the package "AsbruConditions"
 * @author Christian Hinterer
 */
public class AnyPlanPointer implements IPrintable {

	/**
	 * @param anyPlanPointerChild
	 */
	public AnyPlanPointer(IAnyPlanPointerChild anyPlanPointerChild)
	{
		this.anyPlanPointerChild = anyPlanPointerChild;
	}
	
	/**
	 * prints the "asbru element" as valid xml
	 */
	public String print()
	{
		String s = "";
		
		s = "<any-plan-pointer>";
		s = s + anyPlanPointerChild.print();
		s = s + "</any-plan-pointer>";
		
		return s;
	}
	
	private IAnyPlanPointerChild anyPlanPointerChild;
}
