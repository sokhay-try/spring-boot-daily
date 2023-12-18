package com.springbootdaily.services.impl;

import com.springbootdaily.entities.Token;
import com.springbootdaily.entities.User;
import com.springbootdaily.exceptions.APIException;
import com.springbootdaily.payloads.ChangePasswordDto;
import com.springbootdaily.payloads.UpdateUserDto;
import com.springbootdaily.repositories.TokenRepository;
import com.springbootdaily.repositories.UserRepository;
import com.springbootdaily.response.ListResponse;
import com.springbootdaily.response.Pagination;
import com.springbootdaily.response.SuccessResponse;
import com.springbootdaily.services.UserService;
import com.springbootdaily.utils.PaginationUtil;
import com.springbootdaily.utils.SortingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public SuccessResponse getAllUsers(int pageNo, int pageSize, String sortBy) {
        // Create a Pageable instance
        Pageable pageable;

        if (sortBy != null) {
            pageable = PageRequest.of(pageNo, pageSize, SortingUtils.buildSort(sortBy));
        }
        else {
            pageable = PageRequest.of(pageNo, pageSize);
        }

        Page<User> users = this.userRepository.findAll(pageable);

        // get contents
        List<User> contents = users.getContent();


        // set up pagination
        Pagination pagination = PaginationUtil.build(users);

        // set up list response
        ListResponse listResponse = new ListResponse();

        listResponse.setContent(contents);
        listResponse.setPagination(pagination);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(listResponse);

        return successResponse;
    }

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
    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return this.userRepository.findByUsername(username);
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

    @Override
    public User updateUser(Long id, UpdateUserDto updateUserDto) {
        // Find the existing user by ID
        User existingUser = this.userRepository.findById(id).get();
        if(existingUser != null) {
            // Validate that the new email and username are not the same as another user's email and username
            Boolean isEmailAlreadyExist = isEmailAlreadyExist(updateUserDto, existingUser);
            Boolean isUsernameAlreadyExist = isUsernameAlreadyExist(updateUserDto, existingUser);

            if(isUsernameAlreadyExist) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Username is already exist");
            }

            if(isEmailAlreadyExist) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Email is already exist");
            }

            existingUser.setUsername(updateUserDto.getUsername());
            existingUser.setFirstName(updateUserDto.getFirstName());
            existingUser.setLastName(updateUserDto.getLastName());
            existingUser.setEmail(updateUserDto.getEmail());
            existingUser.setPhoneNumber(updateUserDto.getPhoneNumber());

            this.userRepository.save(existingUser);

            return existingUser;

        }
        else {
            throw new APIException(HttpStatus.NOT_FOUND, "Could not find any users with the ID " + id);
        }

    }

    @Override
    public void deleteUser(Long id) {
        // Find the existing user by ID
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            // delete relationship in table users_roles first
            try {
                userRepository.deleteAllRolesByUserId(user.getId());
            }
            catch (JpaSystemException ex) {
                userRepository.delete(user);
            }

        }
    }


    public boolean isEmailAlreadyExist(UpdateUserDto updatedUser, User existingUser) {

        if (!updatedUser.getEmail().equals(existingUser.getEmail())) {
            // Check if the new email is not the same as another user's email
            Optional<User> user = this.userRepository.findByEmail(updatedUser.getEmail());
            if (user.isPresent()) {
                return true;
            }
        }
        return false;

    }
    public boolean isUsernameAlreadyExist(UpdateUserDto updatedUser, User existingUser) {

        if(!updatedUser.getUsername().equals(existingUser.getUsername())) {
            Optional<User> user = this.userRepository.findByUsername(updatedUser.getUsername());

            if (user.isPresent()) {
                return true;
            }
        }
        return false;
    }

}

