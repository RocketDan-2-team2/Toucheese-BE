package com.example.toucheese_be.domain.review.entity;

import com.example.toucheese_be.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StudioReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String imageUrl;

    private String description;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
