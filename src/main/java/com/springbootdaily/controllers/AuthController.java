package com.springbootdaily.controllers;


import com.springbootdaily.entities.User;
import com.springbootdaily.payloads.LoginDto;
import com.springbootdaily.payloads.RegisterDto;
import com.springbootdaily.response.LoginResponse;
import com.springbootdaily.response.SuccessResponse;
import com.springbootdaily.services.AuthService;
import com.springbootdaily.utils.AppConstant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstant.BASE_URL + "auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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
}
