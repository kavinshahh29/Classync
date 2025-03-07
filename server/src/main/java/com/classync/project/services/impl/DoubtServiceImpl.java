package com.classync.project.services.impl;

import com.classync.project.DTO.DoubtDto;
import com.classync.project.DTO.SolutionDto;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.Doubt;
import com.classync.project.entity.Solution;
import com.classync.project.entity.User;
import com.classync.project.repository.ClassRepository;
import com.classync.project.repository.DoubtRepository;
import com.classync.project.repository.SolutionRepository;
import com.classync.project.repository.UserRepository;
import com.classync.project.services.DoubtService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoubtServiceImpl implements DoubtService {

    private final DoubtRepository doubtRepository;
    private final UserRepository userRepository;
    private final ClassRepository classroomRepository;
    private final SolutionRepository solutionRepository;

    public DoubtServiceImpl(DoubtRepository doubtRepository, UserRepository userRepository,
            ClassRepository classroomRepository, SolutionRepository solutionRepository) {
        this.doubtRepository = doubtRepository;
        this.userRepository = userRepository;
        this.classroomRepository = classroomRepository;
        this.solutionRepository = solutionRepository;
    }

    @Override
    public Doubt createDoubt(DoubtDto doubtRequest) {
        System.out.println("Doubt req : " + doubtRequest);
        // Fetch User and Classroom
        User user = userRepository.findById(doubtRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Classroom classroom = classroomRepository.findById(doubtRequest.getClassroomId())
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

        // Create Doubt
        Doubt doubt = new Doubt();
        doubt.setTitle(doubtRequest.getTitle());
        doubt.setContent(doubtRequest.getContent());
        doubt.setCreatedBy(user);
        doubt.setClassroom(classroom);
        doubt.setCreatedAt(LocalDateTime.now());

        return doubtRepository.save(doubt);
    }

    @Override
    public List<Doubt> getAllDoubts() {
        return doubtRepository.findAll();
    }

    @Override
    public Optional<Doubt> getDoubtById(Long id) {
        return doubtRepository.findById(id);
    }

    @Override
    public void deleteDoubt(Long id) {
        doubtRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return doubtRepository.existsById(id);
    }

    @Override
    public List<Doubt> getDoubtsByClassroomId(Long classroomId) {
        return doubtRepository.findByClassroomId(classroomId);
    }

    @Override
    public Solution postSolution(SolutionDto solutionDto) {
        Doubt doubt = doubtRepository.findById(solutionDto.getDoubtId())
                .orElseThrow(() -> new IllegalArgumentException("Doubt not found"));
        User user = userRepository.findById(solutionDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Prevent the user who posted the doubt from posting a solution
        // if (Integer.valueOf(doubt.getCreatedBy().getId()).equals(user.getId())) {
        // throw new IllegalArgumentException("You cannot post a solution to your own
        // doubt.");
        // }

        Solution solution = new Solution(doubt, user, solutionDto.getContent());
        solutionRepository.save(solution);

        return solution;
    }

    @Override
    public List<Solution> getSolutionsByDoubtId(Long doubtId) {
        List<Solution> solutions = solutionRepository.findByDoubtId(doubtId);
        return solutions;
    }
}
