package com.projectsassy.sassy.websoket;

import com.projectsassy.sassy.chattingroom.domain.ChattingRoom;
import com.projectsassy.sassy.chattingroom.repository.ChattingRoomRepository;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChattingRoom findRoom(ChatRequest chatRequest, Long userId) {
        String selectMbti = chatRequest.getSelectMbti();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_USER);
                });

        List<ChattingRoom> findRoom = chattingRoomRepository.findRoomBySelectMbti(selectMbti, user.getMbti());

        if (findRoom.isEmpty()) {
            return createRoom(user, selectMbti);
        }

        ChattingRoom chattingRoom = findRoom.get(0);
        chattingRoom.enterUser(user);

        return chattingRoom;
    }

    public ChattingRoom createRoom(User sendUser, String selectMbti) {
        ChattingRoom chattingRoom = ChattingRoom.of(sendUser, selectMbti);
        chattingRoomRepository.save(chattingRoom);

        return chattingRoom;
    }
}
