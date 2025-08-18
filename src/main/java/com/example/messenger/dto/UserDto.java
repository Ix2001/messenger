package com.example.messenger.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDto(UUID id, String username, String displayName, Instant createdAt) {}
