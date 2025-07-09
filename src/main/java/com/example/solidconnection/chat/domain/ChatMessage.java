package com.example.solidconnection.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
public class ChatMessage {

    private Long roomId;
    private String sender;
    private String message;
    private LocalDateTime sentAt;

    public ChatMessage(Long roomId, String sender, String message, LocalDateTime sentAt) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.sentAt = sentAt;
    }
}
