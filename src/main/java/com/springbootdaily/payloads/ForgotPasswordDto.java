package com.springbootdaily.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordDto {
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email must not be blank")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be a well-formed email address")
    private String email;
}
