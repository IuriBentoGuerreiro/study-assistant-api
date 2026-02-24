package com.ibgs.studyAssistant.studySession.mapper;


import com.ibgs.studyAssistant.studySession.domain.StudySession;
import com.ibgs.studyAssistant.studySession.dto.StudySessionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudySessionMapper {

    @Mapping(target = "questions", ignore = true)
    StudySessionResponseDTO toResponse(StudySession session);
}