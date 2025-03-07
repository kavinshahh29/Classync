package com.classync.project.services;

import com.classync.project.DTO.DoubtDto;
import com.classync.project.DTO.SolutionDto;
import com.classync.project.entity.Doubt;
import com.classync.project.entity.Solution;

import java.util.List;
import java.util.Optional;

public interface DoubtService {
    Doubt createDoubt(DoubtDto doubtRequest);

    List<Doubt> getAllDoubts();

    Optional<Doubt> getDoubtById(Long id);

    void deleteDoubt(Long id);

    boolean existsById(Long id);

    List<Doubt> getDoubtsByClassroomId(Long classroomId);

    Solution postSolution(SolutionDto solutionDto);

    List<Solution> getSolutionsByDoubtId(Long DoubtId);
}
