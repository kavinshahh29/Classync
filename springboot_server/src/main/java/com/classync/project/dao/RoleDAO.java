package com.classync.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classync.project.entity.Role;
import java.util.Optional;

public interface RoleDAO extends JpaRepository<Role , Long>{
    Optional<Role> findByName(String name);
    
}
