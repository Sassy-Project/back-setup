package com.projectsassy.sassy.chatting.repository;

import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    @Query("select r " +
        "from ChattingRoom r " +
        "where r.sendUser.mbti = :myMbti " +
        "group by r.receiveUser.mbti " +
        "order by count(r.receiveUser.mbti) desc")
    List<ChattingRoom> findRecommendMbti(@Param("myMbti") String myMbti);

}
