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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;


@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChattingRoomService chattingRoomService;

    /**
     * /sub/channel/12345  - 구독(channelId:12345)
     * /pub/hello          - 메세지 발행
     */

    @PostMapping("/chat")
    public ResponseEntity createChattingRoom(
            @RequestBody ChatRequest chatRequest,
            @SessionAttribute(name = SessionConst.USER_ID, required = false) Long loginUserId
            ) {
        validateUser(loginUserId);
        ChattingRoom chattingRoom = chattingRoomService.findRoom(chatRequest, loginUserId);

        return ResponseEntity.ok().body(chattingRoom.getId());
    }

    @MessageMapping("/chat/{roomId}") // 클라이언트에서 요청
    public void message(@DestinationVariable("roomId")Long roomId, SocketMessage message) {

        // 메세지에 정의된 채널 id 에 메세지를 보낸다 /sub/channel 를 구독중인 클라이언트에게 메세지를 보낸다
        simpMessageSendingOperations.convertAndSend(
                "/sub/chat/" + roomId, message);
    }

    private static void validateUser(Long loginUserId) {
        if (loginUserId == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }
}
