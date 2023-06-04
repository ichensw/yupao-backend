package cn.ichensw.yupao.utils;

import cn.ichensw.yupao.common.BaseResponse;
import cn.ichensw.yupao.common.ErrorCode;

/**
 * 返回工具类
 *
 * @Author Jucce
 * @date 2022/12/26
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }


    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
