package com.wlb.demo.resource.exception;

/**
 * 业务上的异常
 *
 * @author shiyongbiao
 * @date 2021/03/31 16:36:58
 */
@SuppressWarnings("unused")
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected String msg;
    protected int code = 10000;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public BusinessException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BusinessException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
