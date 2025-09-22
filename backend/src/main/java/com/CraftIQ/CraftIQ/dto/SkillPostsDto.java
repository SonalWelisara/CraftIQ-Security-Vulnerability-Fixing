package com.CraftIQ.CraftIQ.dto;

import com.CraftIQ.CraftIQ.entity.Feedback;
import com.CraftIQ.CraftIQ.entity.User;
import jakarta.persistence.Column;
import lombok.Data;
import org.modelmapper.ModelMapper;
import com.CraftIQ.CraftIQ.entity.SkillPosts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SkillPostsDto {
    private Long id;
    private String title;
    private String summary;
    private String pargrhap1;
    private String pargrhap2;
    private String pargrhap3;
    private String pargrhap4;
    private String pargrhap5;
    private LocalDateTime createdAt;
    private String category;
    private String tags;
    private String imageBase64;


    // List of feedbacks for this post
    private List<FeedbackDto> feedbacks = new ArrayList<>();

    private UserSummaryDto user;

    private List<LikeDto> likes;

    public SkillPosts toEntity(ModelMapper mapper) {
        SkillPosts post = mapper.map(this, SkillPosts.class);

        // Manually map feedbacks to avoid cyclic references
        if (this.feedbacks != null) {
            List<Feedback> feedbackEntities = this.feedbacks.stream().map(dto -> {
                Feedback feedback = dto.toEntity(mapper);
                feedback.setSkillPost(post); // Set the reverse link
                return feedback;
            }).collect(Collectors.toList());

            post.setFeedbacks(feedbackEntities);
        }

        // Link the user to this skill post (you may want to handle user separately based on DTO)
        if (this.user != null) {
            post.setUser(mapper.map(this.user, User.class)); // You can map the user if it's passed
        }

        if (this.imageBase64 != null) {
            post.setImageData(Base64.getDecoder().decode(this.imageBase64));
        }

        return post;
    }
}

