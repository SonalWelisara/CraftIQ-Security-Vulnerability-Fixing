package com.CraftIQ.CraftIQ.entity;

import com.CraftIQ.CraftIQ.dto.MilestoneDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@Entity
public class Milestone {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String date;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "learning_plan_id")
    private LearningPlans learningPlan;

    private MilestoneDto toDto(ModelMapper mapper) {
        MilestoneDto dto = mapper.map(this, MilestoneDto.class);
        return dto;
    }
}

