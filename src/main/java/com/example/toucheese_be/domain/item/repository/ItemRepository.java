package com.example.toucheese_be.domain.item.repository;

import com.example.toucheese_be.domain.item.entity.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom{
    List<Item> findByStudioId(Long studioId);
}
