package com.CraftIQ.CraftIQ.repository;

import com.CraftIQ.CraftIQ.entity.LearningPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningPlansRepository extends JpaRepository<LearningPlans, Long> {
}
