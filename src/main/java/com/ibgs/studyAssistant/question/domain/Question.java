package com.ibgs.studyAssistant.question.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibgs.studyAssistant.common.BaseEntity;
import com.ibgs.studyAssistant.studySession.domain.StudySession;
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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String statement;

    @Column(name = "study_answer")
    private Integer studyAnswer;

    @Column(name = "correct_answer_index", nullable = false)
    private Integer correctAnswerIndex;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "study_session_id", nullable = false)
    private StudySession studySession;
}