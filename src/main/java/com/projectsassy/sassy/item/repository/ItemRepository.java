package com.projectsassy.sassy.item.repository;

import com.projectsassy.sassy.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByDtype(String dtype);
}
