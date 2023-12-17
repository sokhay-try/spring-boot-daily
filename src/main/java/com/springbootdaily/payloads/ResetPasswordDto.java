package com.springbootdaily.payloads;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResetPasswordDto {
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

    @NotNull(message = "Reset password token is required")
    @NotBlank(message = "Reset password token must not be blank")
    @NotEmpty(message = "Reset password token must not be empty")
    private String resetPasswordToken;
}
