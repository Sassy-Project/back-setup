package com.projectsassy.sassy.chatting.controller;

import com.projectsassy.sassy.chatting.dto.RecommendWaitingRequest;
import com.projectsassy.sassy.chatting.service.ChatService;
import com.projectsassy.sassy.chatting.service.MessageService;
import com.projectsassy.sassy.chatting.dto.MessageRequest;
import com.projectsassy.sassy.chatting.dto.WaitingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final MessageService messageService;
    private final ChatService chatService;

    @MessageMapping("/chat/wait")
    public void addUserToWaiting(WaitingRequest waitingRequest, @Header("simpSessionId") String sessionId) {
        chatService.matchUserWithMbti(waitingRequest, sessionId);
    }

    @MessageMapping("/chat/match/{roomId}")
    public void sendMessage(@DestinationVariable("roomId") Long roomId, MessageRequest messageRequest) {
        messageService.sendMessage(roomId, messageRequest);
    }

    @MessageMapping("/chat/wait/recommend")
    public void waitForRecommendedMatching(RecommendWaitingRequest recommendWaitingRequest, @Header("simpSessionId") String sessionId) {
        chatService.matchWithRecommendedUser(recommendWaitingRequest, sessionId);
    }
}
