package com.projectsassy.sassy.chatting.repository;

import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import com.projectsassy.sassy.chatting.dto.MbtiRecommendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    @Query("select new com.projectsassy.sassy.chatting.dto.MbtiRecommendList(ru.mbti, count(*)) " +
        "from ChattingRoom r " +
        "join r.sendUser su " +
        "join r.receiveUser ru " +
        "where su.mbti = :myMbti " +
        "group by ru.mbti")
    List<MbtiRecommendList> findRecommendMbtiBySendUser(@Param("myMbti") String myMbti);

    @Query("select new com.projectsassy.sassy.chatting.dto.MbtiRecommendList(su.mbti, count(*)) " +
        "from ChattingRoom r " +
        "join r.sendUser su " +
        "join r.receiveUser ru " +
        "where ru.mbti = :myMbti and su.mbti != :myMbti " +
        "group by su.mbti")
    List<MbtiRecommendList> findRecommendMbtiByReceiveUser(@Param("myMbti") String myMbti);

}
