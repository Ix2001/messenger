package com.example.messenger.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String displayName,
        @NotBlank String password
) {}
