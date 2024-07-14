package com.fitmate.port.out.common;

import lombok.Getter;

import java.util.List;

@Getter
public class SliceResponse<T> {
    protected final List<T> content;
    protected final Integer currentPage;
    protected final Integer size;
    protected final Boolean first;
    protected final Boolean last;

    public SliceResponse(List<T> content, Integer currentPage, Integer size, Boolean first, Boolean last) {
        this.content = content;
        this.currentPage = currentPage;
        this.size = size;
        this.first = first;
        this.last = last;
    }
}
