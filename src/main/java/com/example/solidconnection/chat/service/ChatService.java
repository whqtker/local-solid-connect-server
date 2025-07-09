package com.example.solidconnection.chat.service;

import com.example.solidconnection.chat.domain.ChatMessage;
import com.example.solidconnection.chat.dto.ChatMessageRequest;
import com.example.solidconnection.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(ChatMessageRequest request, Long roomId) {
        ChatMessage chatMessage = new ChatMessage(
                roomId,
                request.sender(),
                request.message(),
                LocalDateTime.now()
        );

        chatRepository.save(chatMessage);

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + roomId, chatMessage);
    }
}
