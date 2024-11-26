package com.example.toucheese_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String name;

    @Column(length = 150, nullable = false)
    private String profile_img;
}
