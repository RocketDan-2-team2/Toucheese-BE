package com.example.toucheese_be.domain.user.repository;

import com.example.toucheese_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByNickname(String nickname);
}
