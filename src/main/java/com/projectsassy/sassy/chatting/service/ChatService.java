package com.projectsassy.sassy.chatting.service;

import com.projectsassy.sassy.chatting.data.MatchingMbtiData;
import com.projectsassy.sassy.chatting.dto.ChatCloseResponse;
import com.projectsassy.sassy.chatting.dto.MatchResponse;
import com.projectsassy.sassy.chatting.dto.WaitingRequest;
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

        //나의 mbti와 내가 원하는 mbti로 대기 줄 waiting에 mbti 조건으로 추가. -> 이 조건을 이용하여 매칭.
        MatchingMbtiData matchingMbtiData = new MatchingMbtiData(myMbti, selectMbti);
        waitingData.put(sessionId, matchingMbtiData);

        //waiting에 2명 이상이 있을때 매칭 시작
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
                Long roomId = chattingRoomService.createChattingRoom(user, waitingUserId);

                MatchResponse matchResponse = new MatchResponse("match", String.valueOf(roomId));
                simpMessageSendingOperations.convertAndSend("/sub/chat/wait/" + userId, matchResponse);
                simpMessageSendingOperations.convertAndSend("/sub/chat/wait/" + waitingUserId, matchResponse);

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
        simpMessageSendingOperations.convertAndSend("/sub/chat/match/" + roomId, chatCloseResponse);
    }
}
