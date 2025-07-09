package com.example.solidconnection.chat.controller;

import com.example.solidconnection.chat.dto.ChatMessageRequest;
import com.example.solidconnection.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    /*
    테스트용;

    CONNECT
    accept-version:1.2


     */
    @MessageMapping("/chat/test")
    @SendTo("/sub/chat/test")
    public String testConnection() {
        log.info("테스트 연결 메시지 수신됨");
        return "테스트 연결 성공";
    }

    /*
    SEND
    destination:/pub/chat/message/123
    content-type:application/json

    {"sender":"사용자이름","message":"보낼 메시지 내용"}
     */
    @MessageMapping("/chat/message/{roomId}")
    public void handleChatMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest chatMessageRequest
    ) {
        log.info("메시지 수신: {}, 룸 ID: {}", chatMessageRequest, roomId);
        chatService.sendMessage(chatMessageRequest, roomId);
    }
}
