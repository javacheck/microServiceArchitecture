package cn.self.cloud.commonutils.api;

import cn.self.cloud.commonutils.validate.ValidateUtils;

public class RequestUtil {

    private static ThreadLocal<ApiResponse> responseThreadLocal = new ThreadLocal<>();

    public static ApiResponse setResult() {
        ApiResponse apiResponse = new ApiResponse();
        responseThreadLocal.set(apiResponse);

        return apiResponse;
    }

    public static ApiResponse setResult(Object data) {
        ApiResponse apiResponse = new ApiResponse();

        if (ValidateUtils.isEmpty(data)) {
            apiResponse.setMessage("找不到数据");
        } else {
            apiResponse.setData(data);
        }
        responseThreadLocal.set(apiResponse);

        return apiResponse;
    }

    public static ApiResponse setResult(boolean isSuccess) {
        ApiResponse apiResponse = null;
        if (isSuccess) {
            apiResponse = new ApiResponse(0, "操作成功");
        } else {
            apiResponse = new ApiResponse(1, "操作失败");
        }
        responseThreadLocal.set(apiResponse);

        return apiResponse;
    }


    public static ApiResponse setResult(Integer code, String message) {
        ApiResponse apiResponse = new ApiResponse(code, message);
        responseThreadLocal.set(apiResponse);

        return apiResponse;
    }

    public static ApiResponse setResult(Integer code, String message, Object data) {
        ApiResponse apiResponse = new ApiResponse(code, message);

        if (ValidateUtils.isEmpty(data)) {
            apiResponse.setMessage("找不到数据");
        } else {
            apiResponse.setData(data);
        }

        responseThreadLocal.set(apiResponse);

        return apiResponse;
    }

    public static ApiResponse getResult() {

        return responseThreadLocal.get();
    }
}