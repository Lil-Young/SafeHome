package com.ssafy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트 메시지 전송 경로
        registry.enableSimpleBroker("/topic"); // 클라이언트 구독 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("http://localhost:8000", "http://localhost:8080", "http://192.168.205.74:8000", "http://192.168.205.74:8080") // 모든 도메인 허용 (개발 환경)
                .withSockJS(); // SockJS 사용
    }
}
