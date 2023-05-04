package com.projectsassy.sassy.userItem.repository;

import com.projectsassy.sassy.userItem.domain.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    @Query("select ui from UserItem ui " +
        "join fetch ui.user u " +
        "join fetch ui.item i " +
        "where u.id = :userId and i.id = :itemId")
    Optional<UserItem> findUserItem(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
