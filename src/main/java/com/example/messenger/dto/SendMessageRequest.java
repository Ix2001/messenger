package com.example.messenger.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;
public record SendMessageRequest(
        @NotNull UUID chatId,
        @Size(min = 1, max = 4000) String text)
{}

