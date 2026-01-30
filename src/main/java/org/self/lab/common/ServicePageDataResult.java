package org.self.lab.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * service 统一返回的分页对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePageDataResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer count;
    private List<T> list;


    public static <T> ServicePageDataResult<T> empty() {
        return new ServicePageDataResult<>(0, null);
    }


}
