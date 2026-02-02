package org.self.lab.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务状态枚举
 */


@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),

    SERVICE_ERROR(6001, "业务响应异常"),
    SERVICE_NULL(6002, "业务响应为空"),
    CONTROLLER_ERROR(6003, "请求响应异常"),
    METHOD_ARGUMENT_NOT_VALID(6004, "参数校验失败"),

    FAILURE(6000, "未预期错误"),
    CUSTOMIZE_FAILURE(6001, "自定义message"),

    PARAM_IS_NULL(6003, "必要参数校验为空"),
    PARAM_CHECK_FAIL(6004, "必要参数校验失败"),


    ;
    private final Integer code;
    private final String message;

}
