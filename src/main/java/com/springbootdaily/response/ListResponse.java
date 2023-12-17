package com.springbootdaily.response;

import lombok.Data;

@Data
public class ListResponse {
    private Object content;
    private Pagination pagination;
}
