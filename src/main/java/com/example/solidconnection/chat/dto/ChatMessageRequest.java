package com.example.solidconnection.chat.dto;

public record ChatMessageRequest(
        String sender,
        String message
) {
}
