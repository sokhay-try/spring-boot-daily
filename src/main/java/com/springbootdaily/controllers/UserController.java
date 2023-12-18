package com.springbootdaily.controllers;

import com.springbootdaily.entities.User;
import com.springbootdaily.exceptions.APIException;
import com.springbootdaily.payloads.ChangePasswordDto;
import com.springbootdaily.payloads.UpdateUserDto;
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

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(AppConstant.BASE_URL + "users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam Map<String, String> filterParams
    ) {

        SuccessResponse users = this.userService.getAllUsers(pageNo, pageSize, sort, filterParams);

        return ResponseEntity.ok(users);

    }

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

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = this.userService.getUserById(id);

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

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<SuccessResponse> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UpdateUserDto updateUserDto) {

        var updatedUser = this.userService.updateUser(id, updateUserDto);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData(updatedUser);

        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<SuccessResponse> deleteUser(@PathVariable("id") Long id) {

        this.userService.deleteUser(id);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setData("User deleted successfully");
        return ResponseEntity.ok(successResponse);

    }

    @GetMapping("/public-resource")
    public String getPublicResource() {
        return "Public Resource";
    }

    @GetMapping("/admin-resource")
    public String getAdminResource() {
        return "Admin Resource";
    }

}
