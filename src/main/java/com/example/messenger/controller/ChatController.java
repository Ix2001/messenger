package com.example.messenger.controller;

import com.example.messenger.domain.Chat;
import com.example.messenger.domain.ChatMember;
import com.example.messenger.dto.*;
import com.example.messenger.service.ChatService;
import com.example.messenger.service.UserService;
import com.example.messenger.util.Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "chats")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    @Operation(summary = "Создать новый чат", description = "Создает новый чат с указанными участниками")
    @PostMapping
    public ChatDto create(
            @Parameter(description = "Данные для создания чата", required = true)
            @Valid @RequestBody CreateChatRequest req, 
            Authentication auth) {
        var user = userService.getByUsername(auth.getName());
        Chat chat = chatService.create(req, user);
        List<UserDto> members = chatService.membersOf(chat).stream()
                .map(ChatMember::getUser).map(Mapper::toDto).toList();
        return Mapper.toDto(chat, members);
    }

    @Operation(summary = "Добавить участника в чат", description = "Добавляет пользователя в существующий чат")
    @PostMapping("/members")
    public void addMember(
            @Parameter(description = "Данные для добавления участника", required = true)
            @Valid @RequestBody AddMemberRequest req) {
        chatService.addMember(req.chatId(), req.userId());
    }

    @Operation(summary = "Получить чаты пользователя", description = "Возвращает список всех чатов текущего пользователя")
    @GetMapping("/by-user/{userId}")
    public List<ChatDto> chatsOfUser(Authentication auth) {
        var user = userService.getByUsername(auth.getName());
        return chatService.chatsOfUser(user.getId()).stream().map(c -> {
            List<UserDto> members = chatService.membersOf(c).stream()
                    .map(ChatMember::getUser).map(Mapper::toDto).toList();
            return Mapper.toDto(c, members);
        }).toList();
    }
}
