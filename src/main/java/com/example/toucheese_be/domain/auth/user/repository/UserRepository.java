package com.example.toucheese_be.domain.auth.user.repository;

import com.example.toucheese_be.domain.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findById(Long id);
}
