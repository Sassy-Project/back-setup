package com.projectsassy.sassy.item.repository;

import com.projectsassy.sassy.item.domain.Badge;
import com.projectsassy.sassy.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByDtype(String dtype);
}
