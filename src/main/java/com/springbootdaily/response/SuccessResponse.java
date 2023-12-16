package com.springbootdaily.response;

import lombok.Data;

@Data
public class SuccessResponse {

    private String status = "success"; // success
    private Object data;

}
