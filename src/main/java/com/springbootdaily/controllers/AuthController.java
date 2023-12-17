package com.springbootdaily.controllers;


import com.springbootdaily.entities.User;
import com.springbootdaily.payloads.ForgotPasswordDto;
import com.springbootdaily.payloads.LoginDto;
import com.springbootdaily.payloads.RegisterDto;
import com.springbootdaily.payloads.ResetPasswordDto;
import com.springbootdaily.response.LoginResponse;
import com.springbootdaily.response.SuccessResponse;
import com.springbootdaily.services.AuthService;
import com.springbootdaily.services.EmailService;
import com.springbootdaily.services.UserService;
import com.springbootdaily.utils.AppConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(AppConstant.BASE_URL + "auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // Build Login REST API
    @PostMapping(value = {"/login"})
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginDto loginDto){

        LoginResponse loginResponse = authService.login(loginDto);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(loginResponse);

        return ResponseEntity.ok(successResponse);

    }

    // Build Register REST API
    @PostMapping(value = {"/register"})
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto){

        User registerUser = authService.register(registerDto);

        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }

    @PostMapping(value = {"/forgot-password"})
    public ResponseEntity<SuccessResponse> forgotPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto){
        // Generate a unique token
        String resetToken = UUID.randomUUID().toString();
        userService.updateResetPasswordToken(resetToken, forgotPasswordDto.getEmail());

        // Send an email with the reset instructions
        String resetLink = "http://client-app.com/api/v1/auth/reset-password?token=" + resetToken;
        String emailContent = "Please click on the following link to reset your password: " + resetLink;

        this.emailService.sendEmail(forgotPasswordDto.getEmail(), "Password reset request", emailContent);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData("We have sent a reset password link to your email. Please check");

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PostMapping(value = {"/reset-password"})
    public ResponseEntity<?> processResetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {

        User user = this.userService.getByResetPasswordToken(resetPasswordDto.getResetPasswordToken()).get();
        this.userService.updatePassword(user, resetPasswordDto.getPassword());

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData("You have successfully changed your password.");

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}
