package com.CraftIQ.CraftIQ.entity;

import com.CraftIQ.CraftIQ.dto.FeedbackDto;
import com.CraftIQ.CraftIQ.dto.SkillPostsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;


    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "like_count")
    private String likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_post_id", nullable = false)
    private SkillPosts skillPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    public FeedbackDto toDto(ModelMapper mapper) {
        FeedbackDto dto = mapper.map(this, FeedbackDto.class);

        // Manually set skillPostId and userId to avoid null from mapper
        if (this.skillPost != null) {
            dto.setSkillPostId(this.skillPost.getId());
        }

        if (this.user != null) {
            dto.setUserId(this.user.getId());
        }

        return dto;
    }


}
