package com.projectsassy.sassy.chatting.data;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChattingData {

    public static ConcurrentHashMap<Long, String> waiting = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, MatchingMbtiData> waitingData = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> chattingRoomSessions = new ConcurrentHashMap<>();

}
