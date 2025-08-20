package com.example.messenger.domain;

import com.example.messenger.crypto.CryptoStringConverter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import com.example.messenger.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "messages")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    // Ранее было: private UUID chatId;
    // Оставляем ту же колонку БД (chatId), но делаем связь ManyToOne
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chatId", nullable = false) // НЕ меняем имя колонки, чтобы не мигрировать
    private Chat chat;

    // Ранее было: private UUID senderId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "senderId", nullable = false) // тоже оставляем прежнее имя колонки
    private User sender;

    @Convert(converter = CryptoStringConverter.class)
    @Column(name = "text_enc", nullable = false, columnDefinition = "text")
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @CreationTimestamp
    @Column
    private Instant readAt;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileAttachment> fileAttachments;
}
