package com.wf.common.model.request;

import lombok.Data;

import java.util.Objects;

@Data
public class PageRequest {

    public static final int SELECTION_DEFAULT_SIZE = 100;
    public static final int PAGE_DEFAULT_SIZE = 100;
    public static final int HUGE_PAGE_SIZE = 10000;

    private Integer pageNum;

    private Integer pageSize;

    private String sort;

    public PageRequest initPage() {
        if (Objects.isNull(getPageNum())) {
            setPageNum(1);
        }
        if (Objects.isNull(getPageSize())) {
            setPageSize(PAGE_DEFAULT_SIZE);
        }
        return this;
    }

    public PageRequest initPage(Integer size) {
        if (Objects.isNull(getPageNum())) {
            setPageNum(1);
        }
        if (Objects.isNull(size)) {
            setPageSize(PAGE_DEFAULT_SIZE);
        }else {
            setPageSize(size);
        }
        return this;
    }

    public PageRequest nextPage() {
        if (Objects.nonNull(getPageNum())) {
            setPageNum(getPageNum() + 1);
        } else {
            setPageNum(1);
        }
        return this;
    }

}
