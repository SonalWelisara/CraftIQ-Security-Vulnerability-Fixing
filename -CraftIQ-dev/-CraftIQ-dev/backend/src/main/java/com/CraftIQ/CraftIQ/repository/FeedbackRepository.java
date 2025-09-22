package com.CraftIQ.CraftIQ.repository;

import com.CraftIQ.CraftIQ.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // You can define custom queries here if needed
}
