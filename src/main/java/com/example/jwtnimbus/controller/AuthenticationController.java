package com.example.jwtnimbus.controller;

import com.example.jwtnimbus.dto.LoginResponse;
import com.example.jwtnimbus.dto.LoginUserDto;
import com.example.jwtnimbus.dto.RegisterUserDto;
import com.example.jwtnimbus.entity.User;
import com.example.jwtnimbus.service.AuthenticationService;
import com.example.jwtnimbus.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        return ResponseEntity.ok(
                new LoginResponse(jwtToken, jwtService.getExpirationTime())
        );
    }
}
