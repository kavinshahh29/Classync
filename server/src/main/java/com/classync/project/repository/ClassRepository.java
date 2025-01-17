package com.classync.project.repository;

import com.classync.project.entity.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Classroom, Long> {
    Classroom findByClassName(String className); // Example custom query
}
