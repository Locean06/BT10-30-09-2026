package com.example.jwtjjwt.controller;

import com.example.jwtjjwt.dto.LoginResponse;
import com.example.jwtjjwt.dto.LoginUserDto;
import com.example.jwtjjwt.dto.RegisterUserDto;
import com.example.jwtjjwt.entity.User;
import com.example.jwtjjwt.service.AuthenticationService;
import com.example.jwtjjwt.service.JwtService;
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
