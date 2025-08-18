package com.example.messenger.ws.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReadEvent(
        UUID chatId,
        UUID messageId,
        UUID readerId,
        Instant readAt
) {}
