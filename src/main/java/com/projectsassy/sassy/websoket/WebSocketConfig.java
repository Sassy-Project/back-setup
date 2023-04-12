package com.projectsassy.sassy.websoket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket //웹 소켓 서버를 사용하도록 정의
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers (WebSocketHandlerRegistry registry) {
        registry.addHandler(signalingSocketHandler(), "/room")
                .setAllowedOrigins("*");
    }

    @Bean
    private WebSocketHandler signalingSocketHandler() {
        return new com.projectsassy.sassy.websoket.WebSocketHandler();
    }

}
