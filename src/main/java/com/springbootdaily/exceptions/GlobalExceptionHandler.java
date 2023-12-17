package com.springbootdaily.exceptions;

import com.springbootdaily.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex,
                                                              WebRequest webRequest){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // Ex: when call service.getByEmail and not found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // general error
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleAPIException(APIException ex,
                                                              WebRequest webRequest){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // validation error
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        ArrayList errors = this.getAllErrors(ex.getBindingResult().getAllErrors());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Validation errors in your request");
        errorResponse.setErrors(errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // When wrong username or password login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,
                                                                WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Incorrect username or password");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    private ArrayList getAllErrors (List<ObjectError> errors) {
        Map<String, String> allErrors = new HashMap<>();
        errors.forEach((error) ->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            allErrors.put(fieldName, message);
        });

        ArrayList allErrorsArr = new ArrayList<>();
        allErrorsArr.add(allErrors);
        return allErrorsArr;
    }


}
