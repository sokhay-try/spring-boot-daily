package com.springbootdaily.services;

import com.springbootdaily.entities.User;
import com.springbootdaily.payloads.ChangePasswordDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserService {
    Optional<User> getCurrentUser();

    Optional<User> getByEmail(String email);

    void logout(HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication);

    void changePassword(ChangePasswordDto changePasswordDto);

    Optional<User> getByResetPasswordToken(String token);

    void updateResetPasswordToken(String token, String email);

    void updatePassword(User user, String newPassword);
}
