package com.koritski.teamsync.auth.repository;

import com.koritski.teamsync.auth.entity.RoleUser;
import com.koritski.teamsync.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    List<RoleUser> findAllByUser(User user);
}
