package cn.self.cloud.commonutils.api;

public class ApiResponse {
    public final static Integer NEED_LOGIN = 10000;//需要登录

    private Integer code = 0;

    private String message = "成功";

    private Object data;

    public ApiResponse() {

    }

    public ApiResponse(Object data) {
        super();
        this.data = data;
    }

    public ApiResponse(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Integer code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
}
