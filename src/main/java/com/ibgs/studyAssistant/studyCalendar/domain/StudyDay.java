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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_Id")
    private User user;

    private LocalDate studyDate;

    private Integer studiedMinutes;
    private Integer solvedQuestions;

    private boolean completed;
    private LocalDateTime completedAt;
}
