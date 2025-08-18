package com.example.messenger.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddMemberRequest(@NotNull UUID chatId, @NotNull UUID userId) {}
