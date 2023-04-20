package com.projectsassy.sassy.chatting.controller;

import com.projectsassy.sassy.chatting.service.ChattingRoomService;
import com.projectsassy.sassy.chatting.service.MessageService;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import com.projectsassy.sassy.chatting.dto.MatchRequest;
import com.projectsassy.sassy.chatting.data.MatchingMbtiData;
import com.projectsassy.sassy.chatting.dto.MessageRequest2;
import com.projectsassy.sassy.chatting.dto.WaitingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChattingRoomService chattingRoomService;
    private final MessageService messageService;
    private final UserService userService;

//    @PostMapping("/chat")
//    public ResponseEntity createChattingRoom(
//        @RequestBody ChatRequest chatRequest,
//        @SessionAttribute(name = SessionConst.USER_ID, required = false) Long loginUserId
//    ) {
//        validateUser(loginUserId);
//        ChattingRoom chattingRoom = chattingRoomService.findRoomOrCreate(chatRequest, loginUserId);
//
//        return ResponseEntity.ok().body(chattingRoom.getId());
//    }
//
//    @MessageMapping("/chat/{roomId}")
//    public void message(@DestinationVariable("roomId") Long roomId, MessageRequest messageRequest) {
//        //messageRequest 는 어떤값을 넣을지 정해야할듯.
//        String type = messageRequest.getType();
//        if (type.equals("close")) {
//            chattingRoomService.deleteEmptyRoom(messageRequest);
//            simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomId, messageRequest);
//            return;
//        }
//
//        simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomId, messageRequest);
//
//        messageService.saveMessage(roomId, messageRequest);
//
//    }

//    private static void validateUser(Long loginUserId) {
//        if (loginUserId == null) {
//            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
//        }
//    }

    private ConcurrentHashMap<Long, MatchingMbtiData> waiting = new ConcurrentHashMap<>();

    //2번째 방법.
    // ws://localhost:8080/ws/pub/chat/wait  WaitingRequest : type:일단 wait, close 두개로. userId, selectMbti
    // ws://localhost:8080/ws/sub/chat/wait/{userId}  : MatchRequest : type:match, roomId;
    // 둘이 매칭 돼서 roomId를 받음.
    // ws://localhost:8080/ws/pub/chat/match/{roomId}  MessageRequest2 : type:채팅(대화), 나갔을때경우 , roomId, sendUserId, content
    // ws://localhost:8080/ws/sub/chat/match/{roomId}  MessageRequest2 : type:채팅(대화), 나갔을때경우 , roomId, sendUserId, content
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
        MatchingMbtiData matchingMbtiData = new MatchingMbtiData(myMbti, selectMbti);
        waiting.put(userId, matchingMbtiData);
        log.info("size={}", waiting.size());

//        로그
//        System.out.println(waiting.size());
//        System.out.println(waiting.get(userId).getMyMbti());
//        System.out.println(waiting.get(userId).getSelectMbti());

        //waiting에 2명 이상이 있을때
        if (waiting.size() > 1) {
            for (Long waitingUserId : waiting.keySet()) {

                //waiting map에서 for 문을 이용해 탐색할 때 탐색하는 값이 '나' 라면 스킵.
                if (userId == waitingUserId) continue;

                MatchingMbtiData waitingMatchingMbtiData = waiting.get(waitingUserId);
                String waitingUserMbti = waitingMatchingMbtiData.getMyMbti();
                String waitingUserSelectMbti = waitingMatchingMbtiData.getSelectMbti();

                if (selectMbti.equals(waitingUserMbti) && myMbti.equals(waitingUserSelectMbti)) {
                    Long roomId = chattingRoomService.createChattingRoom(user, waitingUserId);

                    //자신의 유저 id값 주소로 type:match, roomId를 보내줌. -> 클라에서 /sub/chat/match/{roomId} 쪽으로 보내줄 수 있는지 확인해보기.
                    //불가능하면 아예 못쓸듯.
                    MatchRequest matchRequest = new MatchRequest("match", String.valueOf(roomId));
                    simpMessageSendingOperations.convertAndSend("/sub/chat/wait/" + userId, matchRequest);
                    simpMessageSendingOperations.convertAndSend("/sub/chat/wait/" + waitingUserId, matchRequest);
                    waiting.remove(userId);
                    waiting.remove(waitingUserId);
                    log.info("size={}", waiting.size());
                    break;
                }

            }
        }
    }

    @MessageMapping("/chat/match/{roomId}")
    public void sendMessage(@DestinationVariable("roomId") Long roomId, MessageRequest2 messageRequest) {//type:여기도 2가지?, roomId, sendUserId, content
        simpMessageSendingOperations.convertAndSend("/sub/chat/match/" + roomId, messageRequest);
        messageService.saveMessage2(roomId, messageRequest);
    }
}
