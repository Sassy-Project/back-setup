package com.projectsassy.sassy.websoket;

import com.projectsassy.sassy.chattingroom.domain.ChattingRoom;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.UnauthorizedException;
import com.projectsassy.sassy.user.domain.SessionConst;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import com.projectsassy.sassy.websoket.second.MatchRequest;
import com.projectsassy.sassy.websoket.second.MbtiCondition;
import com.projectsassy.sassy.websoket.second.WaitingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChattingRoomService chattingRoomService;
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/chat")
    public ResponseEntity createChattingRoom(
        @RequestBody ChatRequest chatRequest,
        @SessionAttribute(name = SessionConst.USER_ID, required = false) Long loginUserId
    ) {
        validateUser(loginUserId);
        ChattingRoom chattingRoom = chattingRoomService.findRoomOrCreate(chatRequest, loginUserId);

        return ResponseEntity.ok().body(chattingRoom.getId());
    }

    @MessageMapping("/chat/{roomId}")
    public void message(@DestinationVariable("roomId") Long roomId, MessageRequest messageRequest) {
        //messageRequest 는 어떤값을 넣을지 정해야할듯.
        String type = messageRequest.getType();
        if (type.equals("close")) {
            chattingRoomService.deleteEmptyRoom(messageRequest);
            simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomId, messageRequest);
            return;
        }

        simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomId, messageRequest);

        messageService.saveMessage(roomId, messageRequest);

    }

    private static void validateUser(Long loginUserId) {
        if (loginUserId == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

    private ConcurrentHashMap<Long, MbtiCondition> waiting = new ConcurrentHashMap<>();

    //2번째 방법.
    @MessageMapping("/chat/wait")  // 유저는 자신의 userId인 /sub/chat/wait/{userId} 에서 대기.
    public void addUserToWaiting(WaitingRequest waitingRequest) { // type:일단 wait, close 두개로. userId, selectMbti
        Long userId = Long.valueOf(waitingRequest.getUserId());

        //대기방에서 type 이 close 라면 소켓 연결이 끊겼다 -> 대기 순위에서 제거.
        if (waitingRequest.getType().equals("close")) {
            waiting.remove(userId);
            System.out.println(waiting.size());
            return;
        }

        User user = userService.findById(userId);
        String myMbti = user.getMbti();
        String selectMbti = waitingRequest.getSelectMbti();

        //나의 mbti와 내가 원하는 mbti로 대기 줄 waiting에 mbti 조건으로 추가. -> 이 조건을 이용하여 매칭.
        MbtiCondition mbtiCondition = new MbtiCondition(myMbti, selectMbti);
        waiting.put(userId, mbtiCondition);
        
//        로그
//        System.out.println(waiting.size());
//        System.out.println(waiting.get(userId).getMyMbti());
//        System.out.println(waiting.get(userId).getSelectMbti());

        //waiting에 2명 이상이 있을때
        if (waiting.size() > 1) {
            for (Long waitingUserId : waiting.keySet()) {
                
                //waiting map에서 for 문을 이용해 탐색할 때 탐색하는 값이 '나' 라면 스킵.
                if (userId == waitingUserId) continue;

                MbtiCondition waitingMbtiCondition = waiting.get(waitingUserId);
                String waitingUserMbti = waitingMbtiCondition.getMyMbti();
                String waitingUserSelectMbti = waitingMbtiCondition.getSelectMbti();

                if (selectMbti.equals(waitingUserMbti) && myMbti.equals(waitingUserSelectMbti)) {
                    Long roomId = chattingRoomService.createChattingRoom(user, waitingUserId);

                    //자신의 유저 id값 주소로 type:match, roomId를 보내줌. -> 클라에서 /sub/chat/match/{roomId} 쪽으로 보내줄 수 있는지 확인해보기.
                    //불가능하면 아예 못쓸듯.
                    MatchRequest matchRequest = new MatchRequest("match", String.valueOf(roomId));
                    simpMessageSendingOperations.convertAndSend("/sub/chat/wait/" + userId, matchRequest);
                    simpMessageSendingOperations.convertAndSend("/sub/chat/wait/" + waitingUserId, matchRequest);
                    waiting.remove(userId);
                    waiting.remove(waitingUserId);
                    break;
                }

            }
        }

    }

    
}
