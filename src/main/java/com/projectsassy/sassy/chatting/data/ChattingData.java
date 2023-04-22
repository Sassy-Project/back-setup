package com.projectsassy.sassy.chatting.data;

import com.projectsassy.sassy.chatting.dto.ChatCloseResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChattingData {

    public static ConcurrentHashMap<Long, String> waiting = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, MatchingMbtiData> waitingData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> chattingRoomSessions = new ConcurrentHashMap<>();

}
