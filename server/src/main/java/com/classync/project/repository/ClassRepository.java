package com.classync.project.repository;

import com.classync.project.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Class findByClassName(String className); // Example custom query
}
