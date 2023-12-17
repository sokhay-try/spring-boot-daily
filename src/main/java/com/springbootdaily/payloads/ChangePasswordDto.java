package com.springbootdaily.payloads;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotNull(message = "Current password is required")
    @NotBlank(message = "Current password must not be blank")
    @NotEmpty(message = "Current password must not be empty")
    private String currentPassword;

    @NotNull(message = "New password is required")
    @NotBlank(message = "New password must not be blank")
    @NotEmpty(message = "New password must not be empty")
    @Size(min = 5, max = 20)
    @Pattern(
            message = "Password contains at least one uppercase letter, one lowercase letter, one number, one symbol and 8 characters",
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    )
    // This regular expression ensures that the password contains at least one uppercase letter, one lowercase letter, one number, and one symbol.
    private String newPassword;

    @NotNull(message = "Confirmation password is required")
    @NotBlank(message = "Confirmation password must not be blank")
    @NotEmpty(message = "Confirmation password must not be empty")
    private String confirmationPassword;

}
