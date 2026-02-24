package com.ibgs.studyAssistant.studyCalendar.mapper;

import com.ibgs.studyAssistant.studyCalendar.domain.Pause;
import com.ibgs.studyAssistant.studyCalendar.dto.pause.PauseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PauseMapper {

    @Mapping(target = "studyDayId", ignore = true)
    PauseResponse toResponse (Pause pause);

    @Mapping(target = "studyDayId", ignore = true)
    List<PauseResponse> toResponse (List<Pause> pause);

}
