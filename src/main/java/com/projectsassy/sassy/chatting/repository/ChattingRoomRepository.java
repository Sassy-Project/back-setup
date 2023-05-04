package com.projectsassy.sassy.chatting.repository;

import com.projectsassy.sassy.chatting.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

}
