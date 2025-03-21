package com.classync.project.controllers;

import com.classync.project.DTO.DoubtDto;
import com.classync.project.DTO.SolutionDto;
import com.classync.project.entity.Doubt;
import com.classync.project.entity.Solution;
import com.classync.project.services.DoubtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/api/doubts")
public class DoubtController {

    private final DoubtService doubtService;

    public DoubtController(DoubtService doubtService) {
        this.doubtService = doubtService;
    }

    @PostMapping("/create")
    public ResponseEntity<Doubt> createDoubt(@RequestBody DoubtDto doubtRequest) {
        Doubt savedDoubt = doubtService.createDoubt(doubtRequest);
        return ResponseEntity.ok(savedDoubt);
    }

    @GetMapping
    public ResponseEntity<List<Doubt>> getAllDoubts() {
        return ResponseEntity.ok(doubtService.getAllDoubts());
    }

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

    @DeleteMapping("/solutions/{solutionId}")
    public ResponseEntity<?> deleteSolution(
            @PathVariable Long solutionId,
            @RequestParam String userEmail) {
        try {
            doubtService.deleteSolution(solutionId, userEmail);
            return ResponseEntity.ok().body("Solution deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete solution.");
        }
    }
}
