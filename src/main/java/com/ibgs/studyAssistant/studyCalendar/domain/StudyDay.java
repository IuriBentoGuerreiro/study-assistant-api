package com.ibgs.studyAssistant.studyCalendar.domain;

import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_day")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyDay extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @Column(name = "study_date", nullable = false)
    private LocalDate studyDate;

    @Column(name = "studied_minutes", nullable = false)
    private Integer studiedMinutes;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
