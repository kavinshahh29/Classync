package com.classync.project.repository;

import com.classync.project.entity.Solution;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByDoubtId(Long doubtId);
}