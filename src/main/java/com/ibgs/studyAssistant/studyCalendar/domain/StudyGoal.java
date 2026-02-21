package com.ibgs.studyAssistant.studyCalendar.domain;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "study_goal")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGoal extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_Id", unique = true)
    private User user;

    @Column(name = "daily_study_seconds", nullable = false)
    private Long dailyStudySeconds;
}
