package com.springbootdaily.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username must not be blank")
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password must not be blank")
    @NotEmpty(message = "Password must not be empty")
    private String password;
}
