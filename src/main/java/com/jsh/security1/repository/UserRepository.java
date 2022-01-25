package com.jsh.security1.repository;

import com.jsh.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username); // JPA Query 메서드
}
