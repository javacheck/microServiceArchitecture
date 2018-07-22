package cn.self.cloud.bean;

public class APIResponse {
	// 默认1表示成功
	private int code = 1;
	private String message;

	public APIResponse() {
	}

	public APIResponse(String message) {
		this.message = message;
	}

	public APIResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public void  set(int code, String message){
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
