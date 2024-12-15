package com.koritski.teamsync.auth.repository;

import com.koritski.teamsync.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);
}
