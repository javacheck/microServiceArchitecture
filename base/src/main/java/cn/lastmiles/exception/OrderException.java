package cn.lastmiles.exception;

public class OrderException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8344674295739837906L;
	
	public OrderException() {
		super();
	}
	public OrderException(String msg) {
		super(msg);
	}
	public OrderException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public OrderException(Throwable cause) {
		super(cause);
	}
}

