package cn.self.cloud.commonutils.api;

import cn.self.cloud.commonutils.validate.ValidateUtils;

public class Verification {

    private String status;

    private String message;

    public Verification(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"" + status + "\":\"" + message + "\"}";
    }

    public static String success(String message) {
        if (ValidateUtils.isEmpty(message)) {
            message = "";
        }
        return new Verification("ok", message).toString();
    }

    public static String error(String message) {
        if (ValidateUtils.isEmpty(message)) {
            message = "";
        }
        return new Verification("error", message).toString();
    }

    public static String error() {
        return error("");
    }

    public static String success() {
        return success("");
    }
}
