package com.classync.project.controllers;

import com.classync.project.DTO.DoubtDto;
import com.classync.project.DTO.SolutionDto;
import com.classync.project.entity.Doubt;
import com.classync.project.entity.Solution;
import com.classync.project.services.DoubtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/doubts")
public class DoubtController {

    private final DoubtService doubtService;

    public DoubtController(DoubtService doubtService) {
        this.doubtService = doubtService;
    }

    // Create a new doubt
    @PostMapping("/create")
    public ResponseEntity<Doubt> createDoubt(@RequestBody DoubtDto doubtRequest) {
        Doubt savedDoubt = doubtService.createDoubt(doubtRequest);
        return ResponseEntity.ok(savedDoubt);
    }

    // Get all doubts
    @GetMapping
    public ResponseEntity<List<Doubt>> getAllDoubts() {
        return ResponseEntity.ok(doubtService.getAllDoubts());
    }

    // Get doubt by ID with error handling
    // @GetMapping("/{id}")
    // public ResponseEntity<?> getDoubtById(@PathVariable Long id) {
    // Optional<Doubt> doubt = doubtService.getDoubtById(id);
    // return doubt.map(ResponseEntity::ok)
    // .orElse(ResponseEntity.status(404).body("Doubt not found"));
    // }

    // Delete a doubt by ID with validation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoubt(@PathVariable Long id) {
        if (!doubtService.existsById(id)) {
            return ResponseEntity.status(404).body("Doubt not found");
        }
        doubtService.deleteDoubt(id);
        return ResponseEntity.ok().body("Doubt deleted successfully");
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<List<Doubt>> getDoubtsByClassroomId(@PathVariable Long classroomId) {
        List<Doubt> doubts = doubtService.getDoubtsByClassroomId(classroomId);
        return ResponseEntity.ok(doubts);
    }

    @PostMapping("/{doubtId}/solution/add")
    public ResponseEntity<?> postSolution(@RequestBody SolutionDto solutionDto) {
        Solution solution = doubtService.postSolution(solutionDto);
        return ResponseEntity.ok(solution);
    }

    @GetMapping("/{doubtId}/solutions")
    public ResponseEntity<?> getSolutionsByDoubtId(@PathVariable Long doubtId) {
        List<Solution> solutions = doubtService.getSolutionsByDoubtId(doubtId);
        return ResponseEntity.ok(solutions);
    }

}
