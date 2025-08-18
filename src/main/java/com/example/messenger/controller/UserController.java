package com.example.messenger.controller;

import com.example.messenger.dto.UserDto;
import com.example.messenger.service.UserService;
import com.example.messenger.util.Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> all() { return userService.all().stream().map(Mapper::toDto).toList(); }

    @GetMapping("/me")
    public UserDto me(Authentication auth) {
        var u = userService.getByUsername(auth.getName());
        return Mapper.toDto(u);
    }

    @GetMapping("/{id}")
    public UserDto one(@PathVariable UUID id) { return Mapper.toDto(userService.get(id)); }
}
