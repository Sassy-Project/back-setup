package com.projectsassy.sassy.chatting.data;

import com.projectsassy.sassy.chatting.dto.ChatCloseResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChattingData {

    public static ConcurrentHashMap<Long, String> waiting = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, MatchingMbtiData> waitingData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> chattingRoomSessions = new ConcurrentHashMap<>();
    public static HashMap<String, Integer> mbtiGraph = new HashMap<>();
    public static int[][] recommendGraph = new int[16][16];
    public static String[] mbti = {"INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP", "ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ"};

    public static void initAddMbti() {
        for (int i = 0; i < mbti.length; i++) {
            mbtiGraph.put(mbti[i], i);
        }
    }
}
