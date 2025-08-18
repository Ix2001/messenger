package com.example.messenger.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatDto(UUID id, String name, boolean groupChat, Instant createdAt, List<UserDto> members) {}
