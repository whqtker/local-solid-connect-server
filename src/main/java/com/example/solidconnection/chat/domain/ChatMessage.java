package com.example.solidconnection.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
public class ChatMessage {

    private String roomId;
    private String sender;
    private String message;
    private ZonedDateTime sentAt;

    public ChatMessage(String roomId, String sender, String message, ZonedDateTime sentAt) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.sentAt = sentAt;
    }
}
