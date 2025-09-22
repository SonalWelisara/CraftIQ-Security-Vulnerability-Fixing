package com.CraftIQ.CraftIQ.entity;

import com.CraftIQ.CraftIQ.dto.LearningPlansDto;
import com.CraftIQ.CraftIQ.dto.MilestoneDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "learning_plans")
public class LearningPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "learning_plan_weeks", joinColumns = @JoinColumn(name = "learning_plan_id"))
    @Column(name = "week")
    private List<String> weeks = new ArrayList<>();



    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Milestones in this learning plan
    @OneToMany(mappedBy = "learningPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Milestone> milestones = new ArrayList<>();



    public LearningPlansDto toDto(ModelMapper mapper) {
        LearningPlansDto dto = mapper.map(this, LearningPlansDto.class);
        if (this.user != null) {
            dto.setUserId(this.user.getId());
        }
        if (this.milestones != null) {
            List<MilestoneDto> milestoneDtos = this.milestones.stream()
                    .map(milestone -> mapper.map(milestone, MilestoneDto.class))
                    .collect(Collectors.toList());
            dto.setMilestones(milestoneDtos);
        }

        dto.setWeeks(this.weeks);


        return dto;
    }

}

