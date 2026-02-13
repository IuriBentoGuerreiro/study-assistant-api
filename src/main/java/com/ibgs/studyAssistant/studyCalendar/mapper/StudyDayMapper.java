package com.ibgs.studyAssistant.studyCalendar.mapper;

import com.ibgs.studyAssistant.studyCalendar.domain.StudyDay;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayRequest;
import com.ibgs.studyAssistant.studyCalendar.dto.studyDay.StudyDayResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudyDayMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "studiedMinutes", ignore = true)
    StudyDay toEntity (StudyDayRequest request);

    @Mapping(source = "user.id", target = "userId")
    StudyDayResponse toResponse (StudyDay studyDay);

    @Mapping(source = "user.id", target = "userId")
    List<StudyDayResponse> toResponse(List<StudyDay> entities);
}
