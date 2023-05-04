package com.projectsassy.sassy.chatting.service;

import com.projectsassy.sassy.chatting.data.ChatConst;
import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import com.projectsassy.sassy.chatting.domain.Message;
import com.projectsassy.sassy.chatting.dto.MessageResponse;
import com.projectsassy.sassy.chatting.repository.MessageRepository;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import com.projectsassy.sassy.chatting.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChattingRoomService chattingRoomService;

    @Transactional
    public void sendMessage(Long roomId, MessageRequest messageRequest) {
        ChattingRoom room = chattingRoomService.findRoomById(roomId);
        User user = userService.findById(Long.valueOf(messageRequest.getSendUserId()));
        String content = messageRequest.getContent();

        Message message = Message.of(room, user, content);
        Message savedMessage = messageRepository.save(message);

        LocalDateTime createdAt = message.getCreatedAt();
        String time = createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));

        MessageResponse messageResponse = new MessageResponse(room.getId(), user.getId(), content, time, user.getNickname(), savedMessage.getId());
        simpMessageSendingOperations.convertAndSend(ChatConst.SUBSCRIBE_MATCH + roomId, messageResponse);
    }
}
