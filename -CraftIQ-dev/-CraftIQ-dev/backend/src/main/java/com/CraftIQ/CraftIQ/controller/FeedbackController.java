package com.CraftIQ.CraftIQ.controller;

import com.CraftIQ.CraftIQ.dto.FeedbackDto;
import com.CraftIQ.CraftIQ.service.Impl.FeedbackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {


    private final FeedbackServiceImpl feedbackService;


    // Create Feedback
    @PostMapping("/create")
    public ResponseEntity<FeedbackDto> createFeedback(@RequestBody FeedbackDto feedbackDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createFeedback(feedbackDto));
    }

    // Get all Feedback
    @GetMapping("/")
    public ResponseEntity<List<FeedbackDto>> getAllFeedback() {
        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.getAllFeedback());
    }

    // Get Feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.getFeedbackById(id));
    }

    // Update Feedback by ID
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDto> updateFeedback(@PathVariable Long id, @RequestBody FeedbackDto feedbackDto) {
        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.updateFeedback(id, feedbackDto));
    }

    // Delete Feedback by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteFeedback(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(feedbackService.deleteFeedback(id));
    }
}
