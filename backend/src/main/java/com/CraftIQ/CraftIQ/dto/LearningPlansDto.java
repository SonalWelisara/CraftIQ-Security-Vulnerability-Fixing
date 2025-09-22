package com.CraftIQ.CraftIQ.dto;

import com.CraftIQ.CraftIQ.entity.Milestone;
import com.CraftIQ.CraftIQ.entity.User;
import lombok.Data;
import org.modelmapper.ModelMapper;
import com.CraftIQ.CraftIQ.entity.LearningPlans;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LearningPlansDto {
    private Long id;
    private String title;
    private String description;

    private String status;

    private Long userId;

    private List<MilestoneDto> milestones;

    private List<String> weeks;

    // Convert DTO to Entity
    public LearningPlans toEntity(ModelMapper mapper) {
        LearningPlans plan = mapper.map(this, LearningPlans.class);

        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            plan.setUser(user);
        }

        if (this.getMilestones() != null) {
            List<Milestone> milestoneEntities = this.getMilestones().stream()
                    .map(dto -> {
                        Milestone milestone = mapper.map(dto, Milestone.class);
                        milestone.setLearningPlan(plan);  // now works because setter is added
                        return milestone;
                    }).collect(Collectors.toList());
            plan.setMilestones(milestoneEntities);
        }

        plan.setWeeks(this.weeks);

        return plan;
    }


}
