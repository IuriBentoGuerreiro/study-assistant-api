package com.ibgs.studyAssistant.studyCalendar.mapper;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyGoal;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyGoal.StudyGoalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyGoalMapper {

    @Mapping(target = "user", ignore = true)
    StudyGoal toEntity (StudyGoalRequest request);

    @Mapping(source = "user.id", target = "userId")
    StudyGoalResponse toResponse(StudyGoal studyGoal);}
