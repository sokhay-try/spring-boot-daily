package com.springbootdaily.controllers;

import com.springbootdaily.entities.User;
import com.springbootdaily.exceptions.APIException;
import com.springbootdaily.payloads.ChangePasswordDto;
import com.springbootdaily.response.ErrorResponse;
import com.springbootdaily.response.SuccessResponse;
import com.springbootdaily.services.UserService;
import com.springbootdaily.utils.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(AppConstant.BASE_URL + "users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = {"/profile"})
    public ResponseEntity<?> getCurrentUser() {
        Optional<User> user = this.userService.getCurrentUser();

        if(user.isPresent()) {
            SuccessResponse successResponse = new SuccessResponse();
            successResponse.setData(user);

            return ResponseEntity.ok(successResponse);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("User not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

    }

    @PostMapping(value = {"/logout"})
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ){
        this.userService.logout(request, response, authentication);
        SuccessResponse successResponse = new SuccessResponse();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping(value = {"/change-password"})
    public ResponseEntity<?> changePassword(
            @RequestBody(required = false) @Valid ChangePasswordDto changePasswordDto
    ) {

        if (changePasswordDto == null) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid format");
        }
        this.userService.changePassword(changePasswordDto);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData("Password changed successfully");

        return ResponseEntity.ok(successResponse);
    }

}
