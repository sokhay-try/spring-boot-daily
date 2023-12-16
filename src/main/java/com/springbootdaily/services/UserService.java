package com.springbootdaily.services;

import com.springbootdaily.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserService {
    Optional<User> getCurrentUser();

    void logout(HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication);
}
