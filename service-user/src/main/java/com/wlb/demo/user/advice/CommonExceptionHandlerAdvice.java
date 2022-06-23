package com.wlb.demo.user.advice;

import com.wlb.demo.user.exception.BusinessException;
import com.wlb.demo.user.model.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * 异常拦截
 *
 * @author Eric Wang
 * @since 2021/03/31 16:37:25
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandlerAdvice {

    public static final String MESSAGE_DEFAULT = "哎呀，有意外了，攻城狮正火速来援";

    public CommonExceptionHandlerAdvice() {
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<JsonResult<String>> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.ok(JsonResult.error(500, MESSAGE_DEFAULT));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<JsonResult<String>> parseException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(JsonResult.error(405, e.getMessage()));
    }

    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.OK)
    public Object processCustomException(BusinessException e) {
        log.error(e.getMessage(), e);
        return JsonResult.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public Object processValidateException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return JsonResult.error(10000, bindErrorMsg(e.getBindingResult().getAllErrors()));
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.OK)
    public Object bindException(BindException e) {
        log.error(e.getMessage(), e);
        return JsonResult.error(10000, bindErrorMsg(e.getBindingResult().getAllErrors()));
    }

    private String bindErrorMsg(List<ObjectError> errors) {
        if (errors != null && errors.size() > 0) {
            return errors.get(0).getDefaultMessage();
        } else {
            return "参数绑定错误";
        }
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    public Object processCustomException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return JsonResult.error(10001, "请求参数有误。");
    }


}
