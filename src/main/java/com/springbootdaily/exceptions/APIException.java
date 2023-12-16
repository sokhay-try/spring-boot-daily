package com.springbootdaily.exceptions;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public APIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
