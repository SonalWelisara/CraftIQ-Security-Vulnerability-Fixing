package com.CraftIQ.CraftIQ.controller;

import com.CraftIQ.CraftIQ.dto.SkillPostsDto;
import com.CraftIQ.CraftIQ.service.Impl.SkillPostsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("api/skillposts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SkillPostsController {

    private final SkillPostsServiceImpl skillPostsService;

    // Create SkillPost
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<SkillPostsDto> postSkillPost(
            @RequestPart("skillPost") SkillPostsDto skillPostsDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                skillPostsDto.setImageBase64(Base64.getEncoder().encodeToString(image.getBytes()));
            }
            SkillPostsDto savedPost = skillPostsService.postSkillPost(skillPostsDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Get all SkillPosts
    @GetMapping("/")
    public ResponseEntity<List<SkillPostsDto>> getAllSkillPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(skillPostsService.getAllSkillPosts());
    }

    // Get SkillPost by ID
    @GetMapping("/{id}")
    public ResponseEntity<SkillPostsDto> getSkillPostById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(skillPostsService.getSkillPostById(id));
    }

    // Update SkillPost by ID
    @PutMapping("/{id}")
    public ResponseEntity<SkillPostsDto> updateSkillPost(@PathVariable Long id, @RequestBody SkillPostsDto skillPostsDto) {
        return ResponseEntity.status(HttpStatus.OK).body(skillPostsService.updateSkillPost(id, skillPostsDto));
    }

    // Delete SkillPost by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteSkillPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(skillPostsService.deleteSkillPost(id));
    }
}

