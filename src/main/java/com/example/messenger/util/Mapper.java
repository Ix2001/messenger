package com.example.messenger.util;

import com.example.messenger.domain.Chat;
import com.example.messenger.domain.FileAttachment;
import com.example.messenger.domain.Message;
import com.example.messenger.domain.User;
import com.example.messenger.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getDisplayName(), u.getCreatedAt());
    }

    public static ChatDto toDto(Chat c, List<UserDto> members) {
        return new ChatDto(c.getId(), c.getName(), c.isGroupChat(), c.getCreatedAt(), members);
    }

    public static MessageDto toDto(Message m) {
        List<FileAttachmentDto> fileAttachments = m.getFileAttachments() != null ?
                m.getFileAttachments().stream()
                        .map(Mapper::toDto)
                        .collect(Collectors.toList()) :
                List.of();

        return new MessageDto(
                m.getId(),
                m.getChat().getId(),
                m.getSender().getId(),
                m.getSender().getDisplayName(),
                m.getText(),
                fileAttachments,
                m.getCreatedAt()
        );
    }

    public static FileAttachmentDto toDto(FileAttachment fa) {
        return FileAttachmentDto.builder()
                .id(fa.getId())
                .fileName(fa.getFileName())
                .originalFileName(fa.getOriginalFileName())
                .contentType(fa.getContentType())
                .fileSize(fa.getFileSize())
                .createdAt(fa.getCreatedAt())
                .build();
    }

    public static FileAttachmentDto toDtoWithDownloadUrl(FileAttachment fa, String downloadUrl) {
        return FileAttachmentDto.builder()
                .id(fa.getId())
                .fileName(fa.getFileName())
                .originalFileName(fa.getOriginalFileName())
                .contentType(fa.getContentType())
                .fileSize(fa.getFileSize())
                .downloadUrl(downloadUrl)
                .createdAt(fa.getCreatedAt())
                .build();
    }
}
