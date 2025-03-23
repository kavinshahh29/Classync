package com.classync.project.dao;

import com.classync.project.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomDAO extends JpaRepository<Classroom, Long> {

    Optional<Classroom> findByClassroomCode(String classroomCode);

    boolean existsByClassroomCode(String classroomCode);

    boolean existsByClassName(String className);
}
