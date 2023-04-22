package com.projectsassy.sassy.chatting.handler;

import com.projectsassy.sassy.chatting.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static com.projectsassy.sassy.chatting.data.ChattingData.*;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {

    private ChatService chatService;

    public StompHandler(@Lazy ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            if (waitingData.get(sessionId) != null) {
                waitingData.remove(sessionId);

                for (Long userId : waiting.keySet()) {
                    if (waiting.get(userId).equals(sessionId)) {
                        waiting.remove(userId);
                        break;
                    }
                }
            }
            if (chattingRoomSessions.get(sessionId) != null) {
                Long roomId = chattingRoomSessions.get(sessionId);
                chatService.sendCloseMessage(roomId);
                chattingRoomSessions.remove(sessionId);
            }

        }

        return message;
    }

}

