package com.movie.ticketing.controller;

import com.movie.ticketing.DTO.AuthRequest;
import com.movie.ticketing.utill.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            return "Invalid username or password";
        }

        // Generate and return JWT token
        return jwtUtil.generateToken(authRequest.getUsername());
    }
}

