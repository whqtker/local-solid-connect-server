package com.example.solidconnection.chat.service;

import com.example.solidconnection.chat.domain.ChatMessage;
import com.example.solidconnection.chat.dto.ChatMessageRequest;
import com.example.solidconnection.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(ChatMessageRequest request, Long roomId) {
        log.info("Sending message to room {}: {}", roomId, request);

        ChatMessage chatMessage = new ChatMessage(
                roomId,
                request.sender(),
                request.message(),
                LocalDateTime.now()
        );

        chatRepository.save(chatMessage);

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + roomId, chatMessage);
    }

    public void broadcastJoinMessage(ChatMessageRequest request, Long roomId) {
        String messageContent = request.sender() + "님이 입장하셨습니다.";
        log.info("Broadcasting join message to room {}: {}", roomId, messageContent);

        ChatMessage joinMessage = new ChatMessage(
                roomId,
                "system",
                messageContent,
                LocalDateTime.now()
        );

        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + roomId, joinMessage);
    }
}
