package com.example.solidconnection.common.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("======= WebSocket 인터셉터 실행 =======");

        // 명령 타입과 상관없이 모든 메시지 로깅
        if (accessor.getCommand() == null) {
            log.info("명령 없음: 메시지 타입 = {}", message.getClass().getName());
        } else {
            log.info("명령: {}", accessor.getCommand());
        }

        // 목적지 로깅
        log.info("목적지: {}", accessor.getDestination());

        // 모든 헤더 출력
        log.info("--- 헤더 정보 시작 ---");
        accessor.getMessageHeaders().forEach((key, value) -> {
            log.info("{}: {}", key, value);
        });
        log.info("--- 헤더 정보 끝 ---");

        // 페이로드 로깅 (있는 경우)
        log.info("--- 페이로드 정보 ---");
        if (message.getPayload() != null) {
            try {
                if (message.getPayload() instanceof byte[]) {
                    String payload = new String((byte[]) message.getPayload());
                    log.info("페이로드(문자열): {}", payload);

                    // 바이트 배열 길이도 출력
                    log.info("페이로드 길이: {} 바이트", ((byte[]) message.getPayload()).length);
                } else {
                    log.info("페이로드(객체): {} (타입: {})",
                            message.getPayload(),
                            message.getPayload().getClass().getName());
                }
            } catch (Exception e) {
                log.error("페이로드 처리 중 오류: {}", e.getMessage(), e);
            }
        } else {
            log.info("페이로드 없음");
        }

        // CONNECT 명령은 명시적으로 처리
        if (accessor.getCommand() != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("✓✓✓ CONNECT 명령 수신됨 ✓✓✓");
            log.info("CONNECT 버전: {}", accessor.getFirstNativeHeader("accept-version"));
        }

        log.info("======= 인터셉터 처리 완료 =======");
        return message;
    }
}
