package exception;


/**
 * @author Rohini Yadav
 *This is the exception class used for raising exception from the skiplist class such as
 *element all ready exists in case of add
 */
public class ElementAllreadyExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ElementAllreadyExistsException(String s) {
		super(s);
	}

}
