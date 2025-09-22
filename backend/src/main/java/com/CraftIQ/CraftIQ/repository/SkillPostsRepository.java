package com.CraftIQ.CraftIQ.repository;

import com.CraftIQ.CraftIQ.entity.SkillPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillPostsRepository extends JpaRepository<SkillPosts, Long> {
    // You can define custom queries here if needed
}
