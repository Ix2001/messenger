package com.example.messenger.controller;

import com.example.messenger.dto.*;
import com.example.messenger.security.JwtService;
import com.example.messenger.service.UserService;
import com.example.messenger.service.auth.CustomUserDetailsService;
import com.example.messenger.util.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService uds;
    private final JwtService jwt;

    @PostMapping("/register")
    @Operation(summary = "Login", description = "Возвращает JWT", security = {})
    public UserDto register(@Valid @RequestBody RegisterRequest req) {
        return Mapper.toDto(userService.register(req));
    }

    @PostMapping("/login")
    @Operation(summary = "Register", security = {})
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var ud = uds.loadUserByUsername(req.username());
        String token = jwt.generateToken(ud);
        var u = userService.getByUsername(req.username());
        return new AuthResponse(token, Mapper.toDto(u));
    }
}
