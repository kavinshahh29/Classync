package com.classync.project.repository;

import com.classync.project.entity.Classroom;
import com.google.common.base.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Classroom, Long> {

    Classroom findByClassName(String className);

    List<Classroom> findAll();

    Optional<Classroom> findByClassroomCode(String classroomCode);

}
