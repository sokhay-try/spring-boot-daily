package com.springbootdaily.payloads;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDto {

    @NotNull(message = "username is required")
    @NotBlank(message = "username must not be blank")
    @NotEmpty(message = "username must not be empty")
    @Size(message = "username must be between 5 and 20 characters", min = 5, max = 20)
    private String username;

    @NotNull(message = "firstName is required")
    @NotBlank(message = "firstName must not be blank")
    @NotEmpty(message = "firstName must not be empty")
    private String firstName;

    @NotNull(message = "lastName is required")
    @NotBlank(message = "lastName must not be blank")
    @NotEmpty(message = "lastName must not be empty")
    private String lastName;

    @NotNull(message = "email is required")
    @NotBlank(message = "email must not be blank")
    @NotEmpty(message = "email must not be empty")
    @Email(message = "email must be a well-formed email address")
    private String email;

    @NotNull(message = "phoneNumber is required")
    @NotBlank(message = "phoneNumber must not be blank")
    @NotEmpty(message = "phoneNumber must not be empty")
    @Pattern(message = "phoneNumber must be contain 9 or 10 digits", regexp = "^\\d{9,10}$")
    private String phoneNumber;

    @NotNull(message = "password is required")
    @NotBlank(message = "password must not be blank")
    @NotEmpty(message = "password must not be empty")
    @Size(min = 5, max = 20)
    @Pattern(
            message = "password contains at least one uppercase letter, one lowercase letter, one number, one symbol and 8 characters",
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )
    // This regular expression ensures that the password contains at least one uppercase letter, one lowercase letter, one number, and one symbol.
    private String password;

}
