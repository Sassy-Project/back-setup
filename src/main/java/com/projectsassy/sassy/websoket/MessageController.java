package com.projectsassy.sassy.websoket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * /sub/channel/12345  - 구독(channelId:12345)
     * /pub/hello          - 메세지 발행
     */

    @MessageMapping("/hello") // 클라이언트에서 요청
    public void message(SocketMessage message) {

        // 메세지에 정의된 채널 id 에 메세지를 보낸다 /sub/channel 를 구독중인 클라이언트에게 메세지를 보낸다
        simpMessageSendingOperations.convertAndSend(
                "/sub/channel" + message.getChannelId(), message);
    }
}
