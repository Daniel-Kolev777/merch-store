package com.merchstore.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {

    private static final String USERNAME_SIZE_ERROR_MESSAGE =
            "Username must be between 2 and 32 symbols";

    private static final String PASSWORD_SIZE_ERROR_MESSAGE =
            "Password must be at least 8 characters";

    private static final String PASSWORD_PATTERN_ERROR_MESSAGE =
            "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";

    private static final String EMAIL_FORMAT_ERROR_MESSAGE =
            "Invalid email format";

    @Size(min = 2, max = 32, message = USERNAME_SIZE_ERROR_MESSAGE)
    private String username;

    @Size(min = 8, message = PASSWORD_SIZE_ERROR_MESSAGE)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = PASSWORD_PATTERN_ERROR_MESSAGE
    )
    private String password;

    @Email(message = EMAIL_FORMAT_ERROR_MESSAGE)
    private String email;

    public @Size(min = 2, max = 32, message = USERNAME_SIZE_ERROR_MESSAGE) String getUsername() {
        return username;
    }

    public @Size(min = 8, message = PASSWORD_SIZE_ERROR_MESSAGE) @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = PASSWORD_PATTERN_ERROR_MESSAGE
    ) String getPassword() {
        return password;
    }

    public @Email(message = EMAIL_FORMAT_ERROR_MESSAGE) String getEmail() {
        return email;
    }
}
