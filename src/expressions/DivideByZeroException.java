package expressions;

public class DivideByZeroException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 43575648675978681L;

	/**
	 * 
	 */

	public DivideByZeroException(String string) {
		super(string);
	}
	
	public DivideByZeroException() {
		super();
	}

}
