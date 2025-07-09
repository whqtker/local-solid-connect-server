package com.example.solidconnection.chat.dto;

import java.time.LocalDateTime;

public record ChatMessageRequest(
        String sender,
        String message,
        LocalDateTime sentAt
) {
}
