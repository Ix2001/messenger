package com.example.messenger.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(UUID id, UUID chatId, UUID senderId, String senderName, String content, Instant createdAt) {}
