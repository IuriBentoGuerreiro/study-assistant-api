package com.ibgs.studyAssistant.question.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibgs.studyAssistant.common.BaseEntity;
import com.ibgs.studyAssistant.question.enuns.QuestionType;
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

    @Column(columnDefinition = "TEXT")
    private String statement;

    @Column(name = "study_answer")
    private Integer studyAnswer;

    @Column(name = "correct_answer_index")
    private Integer correctAnswerIndex;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "study_session_id")
    private StudySession studySession;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestionType type;

}