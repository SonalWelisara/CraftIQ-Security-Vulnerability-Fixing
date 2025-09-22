package com.CraftIQ.CraftIQ.controller;

import com.CraftIQ.CraftIQ.dto.LearningPlansDto;
import com.CraftIQ.CraftIQ.service.Impl.LearningPlansServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/learningPlans")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LearningPlansController {

    private final LearningPlansServiceImpl learningPlansService;

    // Create Learning Plan
    @PostMapping("/create")
    public ResponseEntity<LearningPlansDto> postLearningPlan(@RequestBody LearningPlansDto learningPlansDto) {
        return ResponseEntity.status(HttpStatus.OK).body(learningPlansService.postLearningPlan(learningPlansDto));
    }

    // Get all Learning Plans
    @GetMapping("/")
    public ResponseEntity<List<LearningPlansDto>> getAllLearningPlans() {
        return ResponseEntity.status(HttpStatus.OK).body(learningPlansService.getAllLearningPlans());
    }

    // Get Learning Plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<LearningPlansDto> getLearningPlanById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(learningPlansService.getLearningPlanById(id));
    }

    // Update Learning Plan by ID
    @PutMapping("/{id}")
    public ResponseEntity<LearningPlansDto> updateLearningPlan(@PathVariable Long id, @RequestBody LearningPlansDto learningPlansDto) {
        return ResponseEntity.status(HttpStatus.OK).body(learningPlansService.updateLearningPlan(id, learningPlansDto));
    }

    // Delete Learning Plan by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLearningPlan(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(learningPlansService.deleteLearningPlan(id));
    }
}
