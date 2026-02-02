package org.self.lab.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一的分页请求对象
 */
@Data
public abstract class BaseLimitRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // --- 方案一：分页模式 (Page) ---
    @Min(value = 1, message = "页码不能小于1", groups = PageGroup.class)
    private Integer pageNumber;

    @Min(value = 1, message = "页大小不能小于1", groups = PageGroup.class)
    @Max(value = 50, message = "页大小不能超过50", groups = PageGroup.class)
    private Integer pageSize;

    public Integer getPageNumber() {
        if (null == pageNumber || null == pageSize) return null;
        return (this.pageNumber - 1) * pageSize;
    }

    // --- 方案二：下标模式 (Offset/Limit) ---
    @Min(value = 0, message = "offset不能小于0", groups = OffsetGroup.class)
    private Integer offset;

    @Min(value = 1, message = "limit不能小于1", groups = OffsetGroup.class)
    @Max(value = 50, message = "limit不能超过50", groups = OffsetGroup.class)
    private Integer limit;


    public interface PageGroup {
    }

    public interface OffsetGroup {
    }
}
