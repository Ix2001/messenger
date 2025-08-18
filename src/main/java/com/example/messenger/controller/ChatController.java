package com.example.messenger.controller;

import com.example.messenger.domain.Chat;
import com.example.messenger.domain.ChatMember;
import com.example.messenger.dto.*;
import com.example.messenger.service.ChatService;
import com.example.messenger.service.UserService;
import com.example.messenger.util.Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping
    public ChatDto create(@Valid @RequestBody CreateChatRequest req, Authentication auth) {
        // creatorId должен совпадать с аутентифицированным пользователем
        var me = userService.getByUsername(auth.getName());
        if (!me.getId().equals(req.creatorId()))
            throw new IllegalArgumentException("creatorId must match authenticated user");
        Chat chat = chatService.create(req);
        List<UserDto> members = chatService.membersOf(chat).stream()
                .map(ChatMember::getUser).map(Mapper::toDto).toList();
        return Mapper.toDto(chat, members);
    }

    @PostMapping("/members")
    public void addMember(@Valid @RequestBody AddMemberRequest req) {
        chatService.addMember(req.chatId(), req.userId());
    }

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
