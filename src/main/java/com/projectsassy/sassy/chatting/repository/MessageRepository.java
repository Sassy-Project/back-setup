package com.projectsassy.sassy.chatting.repository;

import com.projectsassy.sassy.chatting.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
