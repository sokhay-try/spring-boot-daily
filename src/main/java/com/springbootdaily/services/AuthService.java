package com.springbootdaily.services;

import com.springbootdaily.entities.User;
import com.springbootdaily.payloads.LoginDto;
import com.springbootdaily.payloads.RegisterDto;
import com.springbootdaily.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginDto loginDto);

    User register(RegisterDto registerDto);
}
