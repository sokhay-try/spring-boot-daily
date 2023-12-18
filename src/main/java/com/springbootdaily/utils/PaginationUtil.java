package com.springbootdaily.utils;

import com.springbootdaily.response.Pagination;
import org.springframework.data.domain.Page;

public class PaginationUtil {

    public static Pagination build(Page<?> obj) {
        // set up pagination
        Pagination pagination = new Pagination();

        pagination.setCurrentPage(obj.getNumber());
        pagination.setPageSize(obj.getSize());
        pagination.setTotalElements(obj.getTotalElements());
        pagination.setTotalPages(obj.getTotalPages());
        pagination.setLast(obj.isLast());

        return pagination;
    }
}
