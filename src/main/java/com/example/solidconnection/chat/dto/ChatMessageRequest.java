package com.example.solidconnection.chat.dto;

import java.time.LocalDateTime;

public record ChatMessageRequest(
        String message,
        String sender,
        LocalDateTime sentAt
) {
}
