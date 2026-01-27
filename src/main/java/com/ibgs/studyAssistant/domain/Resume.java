package com.ibgs.studyAssistant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibgs.studyAssistant.auth.model.User;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resume")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resume extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
