package com.classync.project.repository;

import com.classync.project.entity.Doubt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {
    List<Doubt> findByClassroomId(Long classroomId);
}
