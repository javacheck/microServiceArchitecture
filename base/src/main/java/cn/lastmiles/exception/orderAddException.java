package cn.lastmiles.exception;

public class orderAddException extends OrderException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5914287267671448936L;
	public orderAddException() {
		super();
	}
	public orderAddException(String msg) {
		super(msg);
	}
	public orderAddException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public orderAddException(Throwable cause) {
		super(cause);
	}
	
}
