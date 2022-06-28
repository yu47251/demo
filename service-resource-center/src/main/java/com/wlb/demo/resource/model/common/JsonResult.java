package com.wlb.demo.resource.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一接口Json返回
 *
 * @param <T> 业务数据
 * @author shiyongbiao
 * @since  2021/11/01 14:52:14
 */
@SuppressWarnings("unused")
@Data
public class JsonResult<T> implements Serializable {
    private int code = 0;
    private String message;
    private String tipMsg;
    private T result;

    public static <T> JsonResult<T> ok() {
        JsonResult<T> result = new JsonResult<>();
        result.setMessage("success");
        return result;
    }

    public static <T> JsonResult<T> ok(T resultObj) {
        JsonResult<T> result = new JsonResult<>();
        result.setResult(resultObj);
        result.setMessage("success");
        return result;
    }

    public static <T> JsonResult<T> error(int errCode, String message) {
        JsonResult<T> result = new JsonResult<>();
        result.setCode(errCode);
        result.setMessage(message);
        return result;
    }
}
