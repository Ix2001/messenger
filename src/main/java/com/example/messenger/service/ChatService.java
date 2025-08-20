package com.example.messenger.service;


import com.example.messenger.domain.Chat;
import com.example.messenger.domain.ChatMember;
import com.example.messenger.domain.User;
import com.example.messenger.dto.CreateChatRequest;
import com.example.messenger.repo.ChatMemberRepository;
import com.example.messenger.repo.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chats;
    private final ChatMemberRepository members;
    private final UserService userService;

    @Transactional
    public Chat create(CreateChatRequest req, User user) {
        Chat chat = chats.save(Chat.builder()
                .name(req.name())
                .groupChat(Boolean.TRUE.equals(req.groupChat()))
                .build());

        // добавляем создателя
        members.save(ChatMember.builder().chat(chat).user(user).build());

        // добавляем участников
        if (req.memberIds() != null) {
            for (UUID uid : req.memberIds()) {
                if (uid.equals(user.getId())) continue;
                User u = userService.get(uid);
                members.findByChatAndUser(chat, u).orElseGet(() ->
                        members.save(ChatMember.builder().chat(chat).user(u).build()));
            }
        }
        return chat;
    }

    @Transactional
    public void addMember(UUID chatId, UUID userId) {
        Chat chat = chats.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found: " + chatId));
        User user = userService.get(userId);
        members.findByChatAndUser(chat, user).ifPresent(cm -> { throw new IllegalArgumentException("Already member");});
        members.save(ChatMember.builder().chat(chat).user(user).build());
    }

    public List<ChatMember> membersOf(Chat chat) {
        return members.findByChat(chat);
    }

    public Chat get(UUID chatId) {
        return chats.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found: " + chatId));
    }

    public List<Chat> chatsOfUser(UUID userId) {
        User u = userService.get(userId);
        return members.findByUser(u).stream().map(ChatMember::getChat).toList();
    }
}
