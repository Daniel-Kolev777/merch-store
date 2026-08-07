package com.merchstore.dtos;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginDto {

    @NotEmpty(message = "password can't be empty!")
    private String username;

    @NotEmpty(message = "password can't be empty!")
    private String password;

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
}