package com.CraftIQ.CraftIQ.controller;

import com.CraftIQ.CraftIQ.dto.LikeDto;
import com.CraftIQ.CraftIQ.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<LikeDto> likePost(@RequestParam Long postId, @RequestParam Long userId) {
        return ResponseEntity.ok(likeService.likePost(postId, userId));
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<Void> unlikePost(@RequestParam Long postId, @RequestParam Long userId) {
        likeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> hasLiked(@RequestParam Long postId, @RequestParam Long userId) {
        return ResponseEntity.ok(likeService.hasUserLiked(postId, userId));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getLikeCount(@RequestParam Long postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }
}
