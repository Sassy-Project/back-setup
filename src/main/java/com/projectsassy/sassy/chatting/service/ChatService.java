package com.projectsassy.sassy.chatting.service;

import com.projectsassy.sassy.chatting.data.ChatConst;
import com.projectsassy.sassy.chatting.data.MatchingMbtiData;
import com.projectsassy.sassy.chatting.dto.*;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.projectsassy.sassy.chatting.data.ChattingData.*;
import static com.projectsassy.sassy.chatting.data.ChattingData.waiting;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final UserService userService;
    private final ChattingRoomService chattingRoomService;

    @Transactional
    public void matchUserWithMbti(WaitingRequest waitingRequest, String sessionId) {
        Long userId = Long.valueOf(waitingRequest.getUserId());
        waiting.put(userId, sessionId);

        User user = userService.findById(userId);
        String myMbti = user.getMbti();
        String selectMbti = waitingRequest.getSelectMbti();

        MatchingMbtiData matchingMbtiData = new MatchingMbtiData(myMbti, selectMbti);
        waitingData.put(sessionId, matchingMbtiData);

        if (waiting.size() > 1) {
            startMatching(user, myMbti, selectMbti);
        }
    }

    private void startMatching(User user, String myMbti, String selectMbti) {
        for (Long waitingUserId : waiting.keySet()) {
            Long userId = user.getId();

            if (userId == waitingUserId) continue;

            String findSessionId = waiting.get(waitingUserId);
            MatchingMbtiData waitingMatchingMbtiData = waitingData.get(findSessionId);
            String waitingUserMbti = waitingMatchingMbtiData.getMyMbti();
            String waitingUserSelectMbti = waitingMatchingMbtiData.getSelectMbti();

            if (selectMbti.equals(waitingUserMbti) && myMbti.equals(waitingUserSelectMbti)) {
                RoomInformation roomInformation = chattingRoomService.createChattingRoom(user, waitingUserId);
                Long roomId = roomInformation.getRoomId();

                MatchResponse myMatchResponse = new MatchResponse(ChatConst.MATCH, String.valueOf(roomId), roomInformation.getMatchedUserNickname());
                MatchResponse matchedUserResponse = new MatchResponse(ChatConst.MATCH, String.valueOf(roomId), user.getNickname());
                simpMessageSendingOperations.convertAndSend(ChatConst.SUBSCRIBE_WAIT + userId, myMatchResponse);
                simpMessageSendingOperations.convertAndSend(ChatConst.SUBSCRIBE_WAIT + waitingUserId, matchedUserResponse);

                chattingRoomSessions.put(waiting.get(userId), roomId);
                chattingRoomSessions.put(waiting.get(waitingUserId), roomId);

                waitingData.remove(waiting.get(userId));
                waitingData.remove(waiting.get(waitingUserId));
                waiting.remove(userId);
                waiting.remove(waitingUserId);
                break;
            }

        }
    }

    public void sendCloseMessage(Long roomId) {
        ChatCloseResponse chatCloseResponse = new ChatCloseResponse(roomId);
        simpMessageSendingOperations.convertAndSend(ChatConst.SUBSCRIBE_MATCH + roomId, chatCloseResponse);
    }

    //추천 MBTI 매칭
    public void matchWithRecommendedUser(RecommendWaitingRequest recommendWaitingRequest, String sessionId) {
        Long userId = Long.valueOf(recommendWaitingRequest.getUserId());
        waiting.put(userId, sessionId);

        User user = userService.findById(userId);
        String myMbti = user.getMbti();
        log.info("myMbti={}", myMbti);
        String recommendedMbti = recommendMbti(myMbti);
        log.info("recommendedMbti={}", recommendedMbti);

        MatchingMbtiData matchingMbtiData = new MatchingMbtiData(myMbti, recommendedMbti);
        waitingData.put(sessionId, matchingMbtiData);

        if (waiting.size() > 1) {
            startMatching(user, myMbti, recommendedMbti);
        }
    }

    private String recommendMbti(String myMbti) {
        return chattingRoomService.findRecommendMbti(myMbti);
    }

}
