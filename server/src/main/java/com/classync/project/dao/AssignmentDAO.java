package com.classync.project.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.classync.project.entity.Assignment;
import com.classync.project.entity.Classroom;

@Repository
public interface AssignmentDAO extends JpaRepository<Assignment, Long> {
    List<Assignment> findByClassroom(Classroom classroom);
}
