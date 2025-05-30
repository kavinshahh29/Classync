package com.classync.project.dao;

import com.classync.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(int userId);
}