package com.merchstore.controllers;

import com.merchstore.dtos.user.UserLoginDto;
import com.merchstore.dtos.user.UserRegisterInDto;
import com.merchstore.dtos.user.UserRestoreDto;
import com.merchstore.helpers.MessageConstants;
import com.merchstore.security.JwtUtil;
import com.merchstore.services.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AnonymousController {


    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;


    public AnonymousController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UserService userService
    ) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody UserLoginDto loginDTO
    ) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );


        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        loginDTO.getUsername()
                );


        String token =
                jwtUtil.generateToken(
                        userDetails.getUsername()
                );


        Map<String, String> response = new HashMap<>();

        response.put("token", token);


        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public String register(
            @Valid @RequestBody UserRegisterInDto registerInDto
    ) {

        userService.register(registerInDto);

        return MessageConstants.REGISTER_USER_SUCCESS_MESSAGE;
    }

    @PatchMapping("/restore")
    public ResponseEntity<String> restoreAccount(@RequestBody UserRestoreDto dto) {

        userService.restoreAccount(dto);

        return ResponseEntity.ok(
                "Welcome back, " + dto.getUsername() +
                        "! It's good to have you with us again. Your account is back and ready to go!"
        );
    }
}