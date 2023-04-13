package com.projectsassy.sassy.message.repository;

import com.projectsassy.sassy.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
