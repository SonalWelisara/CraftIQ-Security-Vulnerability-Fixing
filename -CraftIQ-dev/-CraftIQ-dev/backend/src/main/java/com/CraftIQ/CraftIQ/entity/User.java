package com.CraftIQ.CraftIQ.entity;

import com.CraftIQ.CraftIQ.dto.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", unique = false)
    private String fullName;

    @Column(name = "tp_num", unique = false)
    private String tpNum;

    @Column(name = "address", unique = false)
    private String address;

    @Column(name = "category", unique = false)
    private String category;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "interests")
    private String interests;

    @Lob
    @Column(name = "image_data", columnDefinition="LONGBLOB")
    private byte[] imageData;


    @Column(name = "role")
    private String role;

    // Users this user follows
    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    // Users following this user
    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SkillPosts> skillPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LearningPlans> learningPlans = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();





    public UserDto toDto(ModelMapper mapper) {
        UserDto userDto = mapper.map(this, UserDto.class);

        // Manually converting PersistentSet to HashSet
        if (this.getFollowers() != null) {
            Set<UserSummaryDto> followerDtos = new HashSet<>(this.getFollowers().stream()
                    .map(f -> mapper.map(f, UserSummaryDto.class))
                    .collect(Collectors.toSet()));
            userDto.setFollowers(followerDtos);
        }

        if (this.getFollowing() != null) {
            Set<UserSummaryDto> followingDtos = new HashSet<>(this.getFollowing().stream()
                    .map(f -> mapper.map(f, UserSummaryDto.class))
                    .collect(Collectors.toSet()));
            userDto.setFollowing(followingDtos);
        }

        // Adding feedbacks to the DTO if needed
        if (this.getFeedbacks() != null) {
            Set<FeedbackDto> feedbackDtos = this.getFeedbacks().stream()
                    .map(feedback -> mapper.map(feedback, FeedbackDto.class))
                    .collect(Collectors.toSet());
            userDto.setFeedbacks(feedbackDtos);
        }

        // Adding skill posts to the DTO
        if (this.getSkillPosts() != null) {
            Set<SkillPostsDto> skillPostDtos = this.getSkillPosts().stream()
                    .map(post -> mapper.map(post, SkillPostsDto.class))
                    .collect(Collectors.toSet());
            userDto.setSkillPosts(skillPostDtos);
        }

        if (this.getLearningPlans() != null) {
            Set<LearningPlansDto> learningPlansDto = this.getLearningPlans().stream()
                    .map(plan -> mapper.map(plan, LearningPlansDto.class))
                    .collect(Collectors.toSet());
            userDto.setLearningPlans(learningPlansDto);
        }

        // Convert image byte[] to Base64 string
        if (this.imageData != null) {
            userDto.setImageBase64(Base64.getEncoder().encodeToString(this.imageData));
        } else {
            userDto.setImageBase64(null);
        }


        return userDto;
    }

}
