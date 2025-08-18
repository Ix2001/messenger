package com.example.messenger.controller;

import com.example.messenger.domain.Message;
import com.example.messenger.dto.MessageDto;
import com.example.messenger.dto.SendMessageRequest;
import com.example.messenger.service.MessageService;
import com.example.messenger.service.UserService;
import com.example.messenger.util.Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "messages")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate broker;
    private final UserService userService;

    @PostMapping
    public MessageDto send(@Valid @RequestBody SendMessageRequest req, Authentication auth) {
        var me = userService.getByUsername(auth.getName());
        Message m = messageService.send(req.chatId(), me.getUsername(), req.text());
        MessageDto dto = Mapper.toDto(m);
        broker.convertAndSend("/topic/chats/" + dto.chatId(), dto);
        return dto;
    }

    @GetMapping("/by-chat/{chatId}")
    public List<MessageDto> last100(@PathVariable UUID chatId) {
        return messageService.last100(chatId).stream().map(Mapper::toDto).toList();
    }

    @PostMapping("/set-read-message-time/{chatId}/{messageId}")
    public void setReadTime(@Valid @PathVariable UUID chatId, @Valid @PathVariable UUID messageId, Authentication auth) {
        var me = userService.getByUsername(auth.getName());

        messageService.setReadTime(chatId, messageId, me);;
        /*Message m = messageService.send(req.chatId(), me.getUsername(), req.text());
        MessageDto dto = Mapper.toDto(m);
        broker.convertAndSend("/topic/chats/" + dto.chatId(), dto);
        return dto;*/

    }
}
