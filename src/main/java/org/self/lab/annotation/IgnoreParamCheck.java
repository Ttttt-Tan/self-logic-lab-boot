package org.self.lab.annotation;

import org.self.lab.interceptor.ParamValidationInterceptor;

import java.lang.annotation.*;

/**
 * 不用参数校验
 * @see ParamValidationInterceptor
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreParamCheck {
}
