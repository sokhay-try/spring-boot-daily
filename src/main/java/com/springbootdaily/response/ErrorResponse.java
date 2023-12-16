package com.springbootdaily.response;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ErrorResponse {
    private String status = "fail"; // and fail
    private String message; // message detail error
    private ArrayList errors = new ArrayList();
}
