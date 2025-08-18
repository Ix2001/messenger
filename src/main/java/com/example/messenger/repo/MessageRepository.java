package com.example.messenger.repo;

import com.example.messenger.domain.Chat;
import com.example.messenger.domain.Message;
import com.example.messenger.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findTop100ByChatOrderByCreatedAtAsc(Chat chat);
    Optional<Message> findByChatAndId(Chat chat, UUID id);
    List<Message> findByChat(Chat chat);
    List<Message> findByChatIdOrderByCreatedAtAsc(UUID chatId);

    // если нужно «все сообщения пользователя через его чаты»
    List<Message> findByChat_Members_UserOrderByCreatedAtDesc(User user);
}
