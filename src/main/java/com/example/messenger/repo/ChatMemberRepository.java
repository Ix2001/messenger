package com.example.messenger.repo;

import com.example.messenger.domain.Chat;
import com.example.messenger.domain.ChatMember;
import com.example.messenger.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatMemberRepository extends JpaRepository<ChatMember, UUID> {
    List<ChatMember> findByChat(Chat chat);
    List<ChatMember> findByUser(User user);
    Optional<ChatMember> findByChatAndUser(Chat chat, User user);
}
