package com.springbootdaily.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull(message = "username is required")
    @NotBlank(message = "username must not be blank")
    @NotEmpty(message = "username must not be empty")
    private String username;

    @NotNull(message = "password is required")
    @NotBlank(message = "password must not be blank")
    @NotEmpty(message = "password must not be empty")
    private String password;
}
