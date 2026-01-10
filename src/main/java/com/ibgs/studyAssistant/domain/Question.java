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

    @Column(name = "study_answer")
    private Integer studyAnswer;

    @Column(name = "correct_answer_index")
    private Integer correctAnswerIndex;

    @ElementCollection
    private List<String> options;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "study_session_id")
    private StudySession studySession;
}