package com.CraftIQ.CraftIQ.service.Impl;

import com.CraftIQ.CraftIQ.dto.SkillPostsDto;
import com.CraftIQ.CraftIQ.entity.Feedback;
import com.CraftIQ.CraftIQ.entity.SkillPosts;
import com.CraftIQ.CraftIQ.entity.User;
import com.CraftIQ.CraftIQ.exception.NotFoundException;
import com.CraftIQ.CraftIQ.repository.SkillPostsRepository;
import com.CraftIQ.CraftIQ.service.SkillPostsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillPostsServiceImpl implements SkillPostsService {

    private final SkillPostsRepository skillPostsRepository;
    private final ModelMapper mapper;

    // Create SkillPost
    @Override
    @Transactional
    public SkillPostsDto postSkillPost(SkillPostsDto skillPostsDto , MultipartFile image) throws IOException {
        SkillPosts skillPost;

        if (skillPostsDto.getId() != null) {
            // For update case: fetch the existing post first
            skillPost = skillPostsRepository.findById(skillPostsDto.getId())
                    .orElseThrow(() -> new RuntimeException("SkillPost not found"));
            mapper.map(skillPostsDto, skillPost); // update fields except image
        } else {
            // For new post
            skillPost = new SkillPosts();
            mapper.map(skillPostsDto, skillPost);
        }

        // Handle Image Upload
        if (image != null && !image.isEmpty()) {
            skillPost.setImageData(image.getBytes());
        } else if (skillPostsDto.getId() != null) {
            // Preserve existing image if not updated
            SkillPosts existing = skillPostsRepository.findById(skillPostsDto.getId()).orElse(null);
            if (existing != null && existing.getImageData() != null) {
                skillPost.setImageData(existing.getImageData());
            }
        }

        // createdAt fallback
        if (skillPost.getCreatedAt() == null) {
            skillPost.setCreatedAt(LocalDateTime.now());
        }

        // Feedbacks
        if (skillPostsDto.getFeedbacks() != null && !skillPostsDto.getFeedbacks().isEmpty()) {
            List<Feedback> feedbackEntities = skillPostsDto.getFeedbacks().stream()
                    .map(dto -> {
                        Feedback feedback = dto.toEntity(mapper);
                        feedback.setSkillPost(skillPost);
                        return feedback;
                    })
                    .collect(Collectors.toList());

            skillPost.setFeedbacks(feedbackEntities);
        }

        // Link user
        if (skillPostsDto.getUser() != null) {
            User user = mapper.map(skillPostsDto.getUser(), User.class);
            skillPost.setUser(user);
        }

        SkillPosts savedSkillPost = skillPostsRepository.save(skillPost);
        SkillPostsDto savedDto = savedSkillPost.toDto(mapper);

        // Return image back as base64
        if (savedSkillPost.getImageData() != null) {
            savedDto.setImageBase64(Base64.getEncoder().encodeToString(savedSkillPost.getImageData()));
        }

        return savedDto;
    }

    // Get all SkillPosts
    @Override
    public List<SkillPostsDto> getAllSkillPosts() {
        List<SkillPosts> skillPosts = skillPostsRepository.findAll();
        if (skillPosts.isEmpty()) {
            return new ArrayList<>();
        } else {
            return skillPosts.stream()
                    .map(skillPost -> skillPost.toDto(mapper))
                    .toList();
        }
    }

    // Get SkillPost by ID
    @Override
    public SkillPostsDto getSkillPostById(Long id) {
        Optional<SkillPosts> skillPost = skillPostsRepository.findById(id);
        if (skillPost.isPresent()) {
            return skillPost.get().toDto(mapper);
        } else {
            throw new NotFoundException("SkillPost not found with ID: " + id);
        }
    }

    // Update SkillPost by ID
    @Override
    @Transactional
    public SkillPostsDto updateSkillPost(Long id, SkillPostsDto skillPostsDto) {
        SkillPosts existingSkillPost = skillPostsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SkillPost not found with ID: " + id));

        existingSkillPost.setTitle(skillPostsDto.getTitle());
        existingSkillPost.setPargrhap1(skillPostsDto.getPargrhap1());
        existingSkillPost.setPargrhap2(skillPostsDto.getPargrhap2());
        existingSkillPost.setPargrhap3(skillPostsDto.getPargrhap3());
        existingSkillPost.setPargrhap4(skillPostsDto.getPargrhap4());
        existingSkillPost.setPargrhap5(skillPostsDto.getPargrhap5());
        existingSkillPost.setSummary(skillPostsDto.getSummary());
        if (skillPostsDto.getCreatedAt() != null) {
            existingSkillPost.setCreatedAt(skillPostsDto.getCreatedAt());
        }
        existingSkillPost.setCategory(skillPostsDto.getCategory());


        // Optional: update user if applicable
        if (skillPostsDto.getUser() != null) {
            User user = mapper.map(skillPostsDto.getUser(), User.class);
            existingSkillPost.setUser(user);
        }

        // Update feedbacks only if provided
        if (skillPostsDto.getFeedbacks() != null && !skillPostsDto.getFeedbacks().isEmpty()) {
            List<Feedback> updatedFeedbacks = skillPostsDto.getFeedbacks().stream()
                    .map(dto -> {
                        Feedback feedback = dto.toEntity(mapper);
                        feedback.setSkillPost(existingSkillPost);
                        return feedback;
                    })
                    .collect(Collectors.toList());
            existingSkillPost.setFeedbacks(updatedFeedbacks);
        }

        SkillPosts savedSkillPost = skillPostsRepository.save(existingSkillPost);
        return savedSkillPost.toDto(mapper);
    }





    // Delete SkillPost by ID
    @Override
    public Boolean deleteSkillPost(Long id) {
        if (!skillPostsRepository.existsById(id)) {
            throw new NotFoundException("SkillPost not found with ID: " + id);
        }
        skillPostsRepository.deleteById(id);
        return true;
    }
}
