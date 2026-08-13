package com.merchstore.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterInDto {

    private static final String USERNAME_SIZE_ERROR_MESSAGE = "Username must be between 2 and 32 symbols";
    private static final String USERNAME_NOT_EMPTY_ERROR_MESSAGE = "Username can't be empty";
    private static final String PASSWORD_SIZE_ERROR_MESSAGE = "Password must be at least 8 characters";
    private static final String PASSWORD_PATTERN_ERROR_MESSAGE =
            "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";
    public static final String PASSWORD_NOT_EMPTY_ERROR_MESSAGE = "Password can't be empty";
    private static final String EMAIL_NOT_EMPTY_ERROR_MESSAGE = "Email can't be empty";
    private static final String EMAIL_FORMAT_ERROR_MESSAGE = "Invalid email format";


    @NotEmpty(message = USERNAME_NOT_EMPTY_ERROR_MESSAGE)
    @Size(min = 2, max = 32, message = USERNAME_SIZE_ERROR_MESSAGE)
    private String username;

    @NotEmpty(message = PASSWORD_NOT_EMPTY_ERROR_MESSAGE)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = PASSWORD_PATTERN_ERROR_MESSAGE
    )
    @Size(min = 8, message = PASSWORD_SIZE_ERROR_MESSAGE)
    private String password;

    @NotEmpty(message = EMAIL_NOT_EMPTY_ERROR_MESSAGE)
    @Email(message = EMAIL_FORMAT_ERROR_MESSAGE)
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
