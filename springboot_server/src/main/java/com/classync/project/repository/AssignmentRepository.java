package com.classync.project.repository;

import com.classync.project.entity.Assignment;
import com.classync.project.entity.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByClassroom(Classroom classroom);

    Optional<Assignment> findById(Long id);

    List<Assignment> findByDueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Assignment> findByDueDateBetweenAndClassroomId(
            LocalDateTime startDate, LocalDateTime endDate, Long classroom);

}
