package org.self.lab.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * controller 统一的返回对象
 */

@Data
public class BaseResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Integer code = ResultCodeEnum.SUCCESS.getCode();

    private String message = ResultCodeEnum.SUCCESS.getMessage();

    private Object data;

    private Long timestamp=System.currentTimeMillis();


    public BaseResult() {
    }

    public BaseResult(ServiceResult<?> serviceResult) {
        if (null == serviceResult) {
            this.code = ResultCodeEnum.SERVICE_NULL.getCode();
            this.message = ResultCodeEnum.SERVICE_NULL.getMessage();
            return;
        }
        this.code = serviceResult.getCode();
        this.message = serviceResult.getMessage();
        if (null != serviceResult.getData()) {
            this.data = serviceResult.getData();
        }
    }

    public BaseResult(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public BaseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }



}
