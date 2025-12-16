package com.ibgs.studyAssistant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_answer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyAnswer extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "studyAnswer")
    private List<Question> questions = new ArrayList<>();
}

