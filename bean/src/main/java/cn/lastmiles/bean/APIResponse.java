package cn.lastmiles.bean;

/**
 * api响应类
 * 
 * @author hql
 *
 */
public class APIResponse {
	private final static String DEFAULTCODE = "000000";
	private final static String DEFAULTMESSAGE = "成功";

	// 默认000000表示成功
	private String code = DEFAULTCODE;
	private String message;

	private Object data;

	public APIResponse() {
	}

	public APIResponse(String message) {
		this.message = message;
	}

	public APIResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public APIResponse(String code, String message, Object data) {
		this(code, message);
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static APIResponse success() {
		return new APIResponse("成功");
	}

	public static APIResponse success(String msg) {
		return new APIResponse(msg);
	}

	public static APIResponse success(Object data) {
		return new APIResponse(DEFAULTCODE, DEFAULTMESSAGE, data);
	}

	public static APIResponse success(String msg, Object data) {
		APIResponse apiResponse = new APIResponse(msg);
		apiResponse.setData(data);
		return apiResponse;
	}

	public static APIResponse getInstance(String code, String msg) {
		return new APIResponse(code, msg);
	}

	public static APIResponse getInstance(String code, String msg, Object data) {
		return new APIResponse(code, msg, data);
	}
}
