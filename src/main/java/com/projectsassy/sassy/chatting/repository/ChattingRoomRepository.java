package com.projectsassy.sassy.chatting.repository;

import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

//    @Query("select c from ChattingRoom c " +
//        "join fetch c.sendUser su " +
//        "left join c.receiveUser ru " +
//        "where su.mbti = :mbti and ru is null and c.wantMbti = :myMbti")
//    List<ChattingRoom> findRoomBySelectMbti(@Param("mbti") String mbti, @Param("myMbti") String myMbti);

}
