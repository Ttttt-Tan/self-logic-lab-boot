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
    SYSTEM_ERROR(500, "系统响应异常"),


    FAILURE(6000, "未预期错误"),
    CUSTOMIZE_FAILURE(6001, "自定义message"),

    PARAM_IS_NULL(6003, "必要参数校验为空"),
    PARAM_CHECK_FAIL(6004, "必要参数校验失败"),


    ;
    private final Integer code;
    private final String message;
}
