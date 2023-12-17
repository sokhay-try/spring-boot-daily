package com.springbootdaily.utils;

import org.springframework.data.domain.Sort;

public class SortingUtils {

    public static Sort buildSort(String sort) {
        String[] sortValues = sort.split(",");
        if (sortValues != null && sortValues.length > 0) {
            Sort.Order[] orders = new Sort.Order[sortValues.length / 2];

            for (int i = 0, j = 0; i < sortValues.length; i += 2, j++) {
                String field = sortValues[i];
                String direction = sortValues[i + 1];
                orders[j] = new Sort.Order(Sort.Direction.fromString(direction), field);
            }

            return Sort.by(orders);
        } else {
            return Sort.unsorted();
        }
    }
}
