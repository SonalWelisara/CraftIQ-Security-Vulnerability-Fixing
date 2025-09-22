package com.CraftIQ.CraftIQ.repository;

import com.CraftIQ.CraftIQ.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsBySkillPostIdAndUserId(Long postId, Long userId);
    Optional<Like> findBySkillPostIdAndUserId(Long postId, Long userId);
    int countBySkillPostId(Long postId);
}
