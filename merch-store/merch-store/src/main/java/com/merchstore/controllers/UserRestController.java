package com.merchstore.controllers;

import com.merchstore.dtos.UserOutDto;
import com.merchstore.dtos.UserUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.merchstore.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

//    GET    /api/users/me
//    PUT    /api/users/me
//    DELETE /api/users/me

    @GetMapping("/me")
    public UserOutDto me(Authentication authentication) {
        return userService.getCurrentUser(authentication.getName());
    }

    @PutMapping("/me")
    public UserOutDto update(@Valid @RequestBody UserUpdateDto userUpdateDto,
                             Authentication authentication) {
        return userService.updateProfile(userUpdateDto, authentication.getName());
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser(
            Authentication authentication) {

        userService.deleteCurrentUser(authentication.getName());

        return ResponseEntity.ok("Account deleted. The streets will welcome you back.");
    }

}
