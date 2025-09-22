package com.CraftIQ.CraftIQ.service;

import com.CraftIQ.CraftIQ.dto.LearningPlansDto;

import java.util.List;

public interface LearningPlansService {
    public LearningPlansDto postLearningPlan(LearningPlansDto learningPlansDto);
    public List<LearningPlansDto> getAllLearningPlans();
    public LearningPlansDto getLearningPlanById(Long id);
    public LearningPlansDto updateLearningPlan(Long id, LearningPlansDto learningPlansDto);
    public Boolean deleteLearningPlan(Long id);
}
