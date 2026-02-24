package com.ibgs.studyAssistant.studyCalendar.domain;

import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table
@Entity(name = "pause")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pause extends BaseEntity {

    @Column(name = "start_pause")
    private LocalDateTime startPause;

    @Column(name = "end_pause")
    private LocalDateTime endPause;

    @ManyToOne
    @JoinColumn(name = "study_day_id", nullable = false)
    private StudyDay studyDay;
}
