package com.fer.ordermanagement.common.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> content;
    private final int currentPage;
    private final int totalPages;
    private final int totalElements;
    private final int pageSize;
    private final boolean first;
    private final boolean last;

    public PageResponse(Page<T> page){
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getNumberOfElements();
        this.pageSize = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
