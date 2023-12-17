package com.springbootdaily.services.impl;

import com.springbootdaily.entities.Token;
import com.springbootdaily.entities.User;
import com.springbootdaily.exceptions.APIException;
import com.springbootdaily.payloads.ChangePasswordDto;
import com.springbootdaily.repositories.TokenRepository;
import com.springbootdaily.repositories.UserRepository;
import com.springbootdaily.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getCurrentUser() {
        // Get the SecurityContext.
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // Get the Authentication object.
        Authentication authentication = securityContext.getAuthentication();

        // Get the principal of the current authentication.
        return this.userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName());
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        try {
            Token storedToken = this.tokenRepository.findByToken(jwt).get();

            if (storedToken.getToken() != null) {
                Long userId = storedToken.getUser().getId();
                this.tokenRepository.deleteAllTokensByUserId(userId);
                SecurityContextHolder.clearContext();
            }
        }
        catch (Exception ex) {
            logger.error("Exception = " + ex.getMessage());
        }

    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {

        // Get the SecurityContext.
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // Get the Authentication object.
        Authentication authentication = securityContext.getAuthentication();

        User user = this.userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName()).get();

        if(user != null) {

            // check if the current password is correct
            if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
                throw new APIException(HttpStatus.FORBIDDEN, "Current password is incorrect");
            }

            // check if the two new passwords are the same
            if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmationPassword())) {
                throw new APIException(HttpStatus.FORBIDDEN, "New password and confirmation password are not the same");
            }

            try {
                // update password
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                this.userRepository.save(user);

                // clear current user token
                this.tokenRepository.deleteAllTokensByUserId(user.getId());
                SecurityContextHolder.clearContext();
            }
            catch (Exception ex) {
                logger.error("Exception = " + ex.getMessage());
            }
        }
    }

    @Override
    public Optional<User> getByResetPasswordToken(String token) {
        return this.userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
       User user = this.userRepository.findByEmail(email).get();

       if(user != null) {
           user.setResetPasswordToken(token);
           this.userRepository.save(user);
       }
       else {
           throw new APIException(HttpStatus.NOT_FOUND, "Could not find any users with the email " + email);
       }
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        this.userRepository.save(user);
    }

}

