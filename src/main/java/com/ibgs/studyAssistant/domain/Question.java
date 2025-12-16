package com.ibgs.studyAssistant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String statement;

    @ElementCollection
    private List<String> options;

    private Integer correctAnswerIndex;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "study_session_id")
    private StudySession studySession;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "study_answer_id")
    private StudyAnswer studyAnswer;
}