package com.springbootdaily.services.impl;

import com.springbootdaily.entities.Token;
import com.springbootdaily.entities.User;
import com.springbootdaily.repositories.TokenRepository;
import com.springbootdaily.repositories.UserRepository;
import com.springbootdaily.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

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

}

