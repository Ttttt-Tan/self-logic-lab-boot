package org.self.lab.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * service 统一返回的对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Integer code;

    private String message;

    private T data;

    public static <T> ServiceResult<T> success() {
        return ServiceResult.<T>builder()
                .code(ResultCodeEnum.SUCCESS.getCode())
                .message(ResultCodeEnum.SUCCESS.getMessage())
                .build();
    }

    public static <T> ServiceResult<T> success(T obj) {
        return ServiceResult.<T>builder()
                .code(ResultCodeEnum.SUCCESS.getCode())
                .message(ResultCodeEnum.SUCCESS.getMessage())
                .data(obj)
                .build();
    }

    public static <T> ServiceResult<T> failure() {
        return ServiceResult.<T>builder()
                .code(ResultCodeEnum.FAILURE.getCode())
                .message(ResultCodeEnum.FAILURE.getMessage())
                .build();
    }

    public static <T> ServiceResult<T> customizeFailure(String message) {
        return ServiceResult.<T>builder()
                .code(ResultCodeEnum.CUSTOMIZE_FAILURE.getCode())
                .message(message)
                .build();
    }

    public static <T> ServiceResult<T> failure(ResultCodeEnum resultCode) {
        return ServiceResult.<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .build();
    }


    public boolean isSuccess() {
        return ResultCodeEnum.SUCCESS.getCode().equals(this.code);
    }
}
