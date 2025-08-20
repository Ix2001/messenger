package com.example.messenger.service;

import com.example.messenger.domain.Chat;
import com.example.messenger.domain.ChatMember;
import com.example.messenger.domain.Message;
import com.example.messenger.domain.User;
import com.example.messenger.repo.ChatMemberRepository;
import com.example.messenger.repo.MessageRepository;
import com.example.messenger.ws.events.MessageReadEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messages;
    private final ChatService chatService;
    private final UserService userService;
    private final ChatMemberRepository chatMembers;
    private final ApplicationEventPublisher events;

    @Transactional
    public Message send(UUID chatId, String senderUsername, String contentCiphertext) {
        Chat chat = chatService.get(chatId);
        User sender = userService.getByUsername(senderUsername);

        // проверяем, что юзер состоит в чате через членство
        ChatMember membership = chatMembers.findByChatAndUser(chat, sender)
                .orElseThrow(() -> new IllegalArgumentException("Sender is not a member of the chat"));

        Message m = Message.builder()
                .chat(chat)
                .sender(sender)
                .text(contentCiphertext) // здесь уже должен быть шифротекст
                .build();

        return messages.save(m);
    }




    @Transactional
    public void setReadTime(UUID chatId, UUID messageId, User user) {
        Chat chat = chatService.get(chatId);

        Message message = messages.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        // Сообщение должно принадлежать тому же чату
        if (!message.getChat().getId().equals(chat.getId())) {
            throw new IllegalArgumentException("Message does not belong to chat: " + chatId);
        }

        // Пользователь должен быть участником чата
        boolean isMember = user.getMemberships().stream()
                .anyMatch(m -> m.getChat().getId().equals(chat.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("User is not a member of the chat");
        }
        // Устанавливаем время прочтения только если оно еще не установлено
        if (message.getReadAt() == null) {
            message.setReadAt(Instant.now());
            messages.save(message);
            events.publishEvent(new MessageReadEvent(
                    chat.getId(),
                    message.getId(),
                    user.getId(),
                    Instant.now()
            ));
        }

    }

    public List<Message> last100(UUID chatId) {
        Chat chat = chatService.get(chatId);
        return messages.findTop100ByChatOrderByCreatedAtAsc(chat);
    }
}
