package com.projectsassy.sassy.chatting.service;

import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import com.projectsassy.sassy.chatting.dto.RoomInformation;
import com.projectsassy.sassy.chatting.repository.ChattingRoomRepository;
import com.projectsassy.sassy.common.code.ErrorCode;
import com.projectsassy.sassy.common.exception.CustomIllegalStateException;
import com.projectsassy.sassy.user.domain.User;
import com.projectsassy.sassy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserService userService;

    public ChattingRoom findRoomById(Long roomId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
            .orElseThrow(() -> {
                throw new CustomIllegalStateException(ErrorCode.NOT_FOUND_ROOM);
            });
        return chattingRoom;
    }

    @Transactional
    public RoomInformation createChattingRoom(User sendUser, Long waitingUserId) {
        User receiveUser = userService.findById(waitingUserId);
        ChattingRoom chattingRoom = chattingRoomRepository.save(new ChattingRoom(sendUser, receiveUser));
        return new RoomInformation(chattingRoom.getId(), receiveUser.getNickname());
    }

    public String findRecommendMbti(String myMbti) {
        List<ChattingRoom> recommendMbti = chattingRoomRepository.findRecommendMbti(myMbti);
        if (recommendMbti.isEmpty()) {
            return basicRecommend(myMbti);
        }

        return recommendMbti.get(0).getReceiveUser().getMbti();
    }

    private String basicRecommend(String myMbti) {
        char firstIndex = myMbti.charAt(0);
        char lastIndex = myMbti.charAt(3);

        if (firstIndex == 'E') firstIndex = 'I';
        else firstIndex = 'E';

        if (lastIndex == 'P') lastIndex = 'J';
        else lastIndex = 'P';

        String basicRecommendedMbti = firstIndex + myMbti.substring(1, 3) + lastIndex;
        return basicRecommendedMbti;
    }
}
