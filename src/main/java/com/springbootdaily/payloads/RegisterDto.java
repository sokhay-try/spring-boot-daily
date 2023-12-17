package com.springbootdaily.payloads;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDto {

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username must not be blank")
    @NotEmpty(message = "Username must not be empty")
    @Size(message = "Username must be between 5 and 20 characters", min = 5, max = 20)
    private String username;

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name must not be blank")
    @NotEmpty(message = "First name must not be empty")
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotBlank(message = "Last name must not be blank")
    @NotEmpty(message = "Last name must not be empty")
    private String lastName;

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email must not be blank")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be a well-formed email address")
    private String email;

    @NotNull(message = "Phone number is required")
    @NotBlank(message = "Phone number must not be blank")
    @NotEmpty(message = "Phone number must not be empty")
    @Pattern(message = "Phone number must be contain 9 or 10 digits", regexp = "^\\d{9,10}$")
    private String phoneNumber;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password must not be blank")
    @NotEmpty(message = "Password must not be empty")
    @Size(min = 5, max = 20)
    @Pattern(
            message = "Password contains at least one uppercase letter, one lowercase letter, one number, one symbol and 8 characters",
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )
    // This regular expression ensures that the password contains at least one uppercase letter, one lowercase letter, one number, and one symbol.
    private String password;

}
