package com.projectsassy.sassy.websoket;

import com.projectsassy.sassy.chattingroom.domain.ChattingRoom;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.UnauthorizedException;
import com.projectsassy.sassy.user.domain.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private ConcurrentHashMap<String, String> waiting = new ConcurrentHashMap<>();
    private final ChattingRoomService chattingRoomService;

    @PostMapping("/chat")
    public ResponseEntity createChattingRoom(
        @RequestBody ChatRequest chatRequest,
        @SessionAttribute(name = SessionConst.USER_ID, required = false) Long loginUserId
    ) {
        validateUser(loginUserId);
        ChattingRoom chattingRoom = chattingRoomService.findRoom(chatRequest, loginUserId);

        return ResponseEntity.ok().body(chattingRoom.getId());
    }

    @MessageMapping("/chat/{roomId}")
    public void message(@DestinationVariable("roomId") Long roomId, MessageRequest messageRequest) {

        //messageRequest 는 어떤값을 넣을지 정해야할듯.
        simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomId, messageRequest);

    }

    private static void validateUser(Long loginUserId) {
        if (loginUserId == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

}
