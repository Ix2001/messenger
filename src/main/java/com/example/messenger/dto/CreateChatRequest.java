package com.example.messenger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateChatRequest(
        @NotBlank String name,
        @NotNull Boolean groupChat,
        List<UUID> memberIds
) {}
