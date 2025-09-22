package com.CraftIQ.CraftIQ.dto;

import com.CraftIQ.CraftIQ.entity.LearningPlans;
import com.CraftIQ.CraftIQ.entity.Milestone;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class MilestoneDto {

    private Long id;
    private String title;
    private String date;
    private boolean completed;

    public Milestone toEntity(ModelMapper mapper) {
        Milestone milestone = mapper.map(this, Milestone.class);

        return milestone;
    }
}
