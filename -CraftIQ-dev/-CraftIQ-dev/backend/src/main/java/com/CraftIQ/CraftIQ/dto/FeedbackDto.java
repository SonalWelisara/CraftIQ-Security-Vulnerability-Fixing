package com.CraftIQ.CraftIQ.dto;

import com.CraftIQ.CraftIQ.entity.Feedback;
import com.CraftIQ.CraftIQ.entity.SkillPosts;
import com.CraftIQ.CraftIQ.entity.User;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
public class FeedbackDto {

    private Long id;
    private String comment;
    private String author;
    private LocalDateTime createdAt;
    private String likeCount;

    private Long skillPostId;
    private Long userId;

    public Feedback toEntity(ModelMapper mapper) {
        Feedback feedback = mapper.map(this, Feedback.class);

        // Set SkillPost from ID
        if (this.skillPostId != null) {
            SkillPosts post = new SkillPosts();
            post.setId(this.skillPostId);
            feedback.setSkillPost(post);
        }

        // Set User from ID
        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            feedback.setUser(user);
        }

        return feedback;
    }
}
