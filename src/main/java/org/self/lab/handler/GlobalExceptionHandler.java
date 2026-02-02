package org.self.lab.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.self.lab.common.BaseResult;
import org.self.lab.common.ResultCodeEnum;
import org.self.lab.exception.SelfBusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * controller 异常捕获器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的系统异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e, HttpServletRequest request) {
        log.error("URL: [{}], 系统未知异常: ", request.getRequestURI(), e);
        return new BaseResult(ResultCodeEnum.CONTROLLER_ERROR);
    }

    /**
     * 捕获自定义业务异常
     */
    @ExceptionHandler(SelfBusinessException.class)
    public BaseResult handleBusinessException(SelfBusinessException e) {
        log.error("业务异常捕获: {}", e.getMessage());
        return new BaseResult(ResultCodeEnum.SERVICE_ERROR);
    }

    /**
     * spring参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return new BaseResult(ResultCodeEnum.CUSTOMIZE_FAILURE.getCode(), defaultMessage);
        }
        return new BaseResult(ResultCodeEnum.METHOD_ARGUMENT_NOT_VALID);
    }
}
