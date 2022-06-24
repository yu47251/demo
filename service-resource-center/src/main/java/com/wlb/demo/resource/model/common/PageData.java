package com.wlb.demo.resource.model.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 分页对象
 *
 * @param <T>
 * @author zhouhui
 */
@SuppressWarnings("unused")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageData<T> implements Serializable {
    
    private static final long serialVersionUID = 7154044169245161300L;
    /**
     * 当前页
     */
    private long pageNum;
    /**
     * 每页记录数
     */
    private long pageSize;
    /**
     * 起始行
     */
    private long startRow;
    /**
     * 末行
     */
    private long endRow;
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private long pages;

    /**
     * 数据
     */
    private List<T> data = new ArrayList<>();

    public <S> PageData(IPage<S> p, Function<S, T> converter) {
        this.pageNum = p.getCurrent();
        this.pageSize = p.getSize();
        this.total = p.getTotal();
        this.pages = p.getPages();
        if (p.getRecords() != null) {
            this.data = p.getRecords().stream().map(converter).collect(Collectors.toList());
        }

    }

    public PageData(long pageNum, long pageSize, long total, long pages) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
    }

    public static <S, T> PageData<T> from(IPage<S> p, Function<S, T> converter) {
        PageData<T> pd = new PageData<>(p.getCurrent(), p.getSize(), p.getTotal(), p.getPages());
        if (p.getRecords() != null) {
            pd.data = p.getRecords().stream().map(converter).collect(Collectors.toList());
        }
        return pd;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
        if (this.pageSize != 0) {
            this.pages = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
        }
    }

    public long getStartRow() {
        return startRow;
    }

    public void setStartRow(long startRow) {
        this.startRow = startRow;
    }

    public long getEndRow() {
        return endRow;
    }

    public void setEndRow(long endRow) {
        this.endRow = endRow;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        if (this.pageSize != 0) {
            this.pages = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
        }
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + total +
                ", pages=" + pages +
                ", data=" + data +
                '}';
    }
}
