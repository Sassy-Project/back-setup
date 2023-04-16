package com.projectsassy.sassy.websoket;

import com.projectsassy.sassy.chattingroom.domain.ChattingRoom;
import com.projectsassy.sassy.message.domain.Message;
import com.projectsassy.sassy.message.repository.MessageRepository;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import com.projectsassy.sassy.websoket.second.MessageRequest2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChattingRoomService chattingRoomService;

    @Transactional
    public void saveMessage(Long roomId, MessageRequest messageRequest) {
        User user = userService.findById(Long.valueOf(messageRequest.getSendUserId()));
        ChattingRoom room = chattingRoomService.findRoomById(roomId);
        String content = messageRequest.getContent();

        Message message = Message.of(room, user, content);
        messageRepository.save(message);
    }

    @Transactional
    public void saveMessage2(Long roomId, MessageRequest2 messageRequest) {
        ChattingRoom room = chattingRoomService.findRoomById(roomId);
        User user = userService.findById(Long.valueOf(messageRequest.getSendUserId()));
        String content = messageRequest.getContent();

        Message message = Message.of(room, user, content);
        messageRepository.save(message);
    }
}
