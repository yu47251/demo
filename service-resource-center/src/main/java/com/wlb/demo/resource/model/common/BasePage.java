package com.wlb.demo.resource.model.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * BasePage
 *
 * @author shiyongbiao
 * @date 2021/11/01 14:52:14
 */
@Data
public class BasePage {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    @SuppressWarnings({"unused", "AlibabaLowerCamelCaseVariableNaming"})
    public <T> IPage<T> toIPage() {
        return new Page<>(getPageNum(), getPageSize(), true);
    }

}
