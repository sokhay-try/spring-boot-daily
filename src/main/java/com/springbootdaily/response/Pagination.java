package com.springbootdaily.response;

import lombok.Data;

@Data
public class Pagination {
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
