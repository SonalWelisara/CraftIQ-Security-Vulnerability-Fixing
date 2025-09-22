package com.CraftIQ.CraftIQ.entity;

import com.CraftIQ.CraftIQ.dto.LikeDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@Entity
@Table(name = "post_likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_post_id")
    private SkillPosts skillPost;

    public LikeDto toDto(ModelMapper mapper) {
        LikeDto likeDto = mapper.map(this, LikeDto.class);
        return likeDto;
    }
}
