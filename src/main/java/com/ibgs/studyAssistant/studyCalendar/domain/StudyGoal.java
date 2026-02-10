package com.ibgs.studyAssistant.studyCalendar.domain;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "study_goal",   uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "study_date"}
    )
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGoal extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    private Integer dailyStudyMinutes;
    private Integer dailyQuestionsTarget;
    private LocalDate startDate;
    private LocalDate endDate;

    private boolean active;
}
