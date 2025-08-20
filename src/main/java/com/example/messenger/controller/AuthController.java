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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationProvider authProvider;
    private final CustomUserDetailsService uds;
    private final JwtService jwt;

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Регистрация нового пользователя", security = {})
    public UserDto register(@Valid @RequestBody RegisterRequest req) {
        return Mapper.toDto(userService.register(req));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Вход в систему, возвращает JWT", security = {})
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        authProvider.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var ud = uds.loadUserByUsername(req.username());
        String token = jwt.generateToken(ud);
        var u = userService.getByUsername(req.username());
        return new AuthResponse(token, Mapper.toDto(u));
    }
}
