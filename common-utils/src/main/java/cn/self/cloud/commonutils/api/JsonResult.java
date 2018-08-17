package cn.self.cloud.commonutils.api;

import java.util.HashMap;

/**
 * Created by cp on 2017/7/31.
 */
public class JsonResult<T> {
    private int code; //所有返回码都归结到此字段，data部分不再设置业务返回码result ，
    // 此字段定义：100以内的错误码为内部使用业务码  1 成功  -1 模块位找到 -2 接口未找到  -3 未知错误 其他为标准的http返回码
    private String msg;
    private T data;

    /**
     * 获取返回空数组的JsonResult对象
     *
     * @param code
     * @param msg
     * @return
     */
    public static JsonResult defaultArrayJsonResult(int code, String msg) {
        return new JsonResult(code, msg, new int[0]);
    }

    public static JsonResult defaultArrayJsonResult() {
        return new JsonResult(1, "没有查到数据", new int[0]);
    }

    /***
     * 获取返回空对象的JsonResult对象
     *
     * @param code
     * @param msg
     * @return
     */
    public static JsonResult defaultObjectJsonResult(int code, String msg) {
        return new JsonResult(code, msg, new HashMap());
    }

    public static JsonResult defaultObjectJsonResult() {
        return new JsonResult(1, "没有找到对象", new HashMap());
    }

    public JsonResult() {

    }

    public JsonResult(int code, String msg) {
        this(code, msg, null);
    }

    public JsonResult(T data) {
        this(1, "ok", data);
    }

    public JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
