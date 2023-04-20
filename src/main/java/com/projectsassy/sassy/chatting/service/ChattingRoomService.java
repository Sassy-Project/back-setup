package com.projectsassy.sassy.chatting.service;

import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import com.projectsassy.sassy.chatting.repository.ChattingRoomRepository;
import com.projectsassy.sassy.chatting.dto.MessageRequest;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserService userService;

//    @Transactional
//    public ChattingRoom findRoomOrCreate(ChatRequest chatRequest, Long userId) {
//        String selectMbti = chatRequest.getSelectMbti();
//        User user = userService.findById(userId);
//
//        List<ChattingRoom> findRoom = chattingRoomRepository.findRoomBySelectMbti(selectMbti, user.getMbti());
//
//        if (findRoom.isEmpty()) {
//            return createRoom(user, selectMbti);
//        }
//
//        ChattingRoom chattingRoom = findRoom.get(0);
//        chattingRoom.enterUser(user);
//
//        return chattingRoom;
//    }

//    @Transactional
//    public ChattingRoom createRoom(User sendUser, String selectMbti) {
//        ChattingRoom chattingRoom = ChattingRoom.of(sendUser, selectMbti);
//        chattingRoomRepository.save(chattingRoom);
//
//        return chattingRoom;
//    }

    @Transactional
    public void deleteEmptyRoom(MessageRequest messageRequest) {
        Long roomId = Long.valueOf(messageRequest.getRoomId());

        ChattingRoom chattingRoom = findRoomById(roomId);

        if (chattingRoom.getReceiveUser() == null) {
            chattingRoomRepository.delete(chattingRoom);
        }
    }

    public ChattingRoom findRoomById(Long roomId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_ROOM);
            });
        return chattingRoom;
    }

    @Transactional
    public Long createChattingRoom(User sendUser, Long waitingUserId) {
        User receiveUser = userService.findById(waitingUserId);
        ChattingRoom chattingRoom = chattingRoomRepository.save(new ChattingRoom(sendUser, receiveUser));
        return chattingRoom.getId();
    }
}
