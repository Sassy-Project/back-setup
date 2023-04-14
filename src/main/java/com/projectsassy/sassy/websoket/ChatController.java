package com.projectsassy.sassy.websoket;

import com.projectsassy.sassy.chattingroom.domain.ChattingRoom;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.UnauthorizedException;
import com.projectsassy.sassy.user.domain.SessionConst;
import com.projectsassy.sassy.websoket.second.WaitingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private ConcurrentHashMap<String, String> waiting = new ConcurrentHashMap<>();
    private final ChattingRoomService chattingRoomService;
    private final MessageService messageService;

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

    //2번째 방법.
    @MessageMapping("/chat/wait")
    public void addUserToWaiting(WaitingRequest waitingRequest, SimpMessageHeaderAccessor headerAccessor) {

    }
}
