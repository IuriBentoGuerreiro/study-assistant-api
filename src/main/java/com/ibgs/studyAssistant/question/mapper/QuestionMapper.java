package com.ibgs.studyAssistant.question.mapper;

import com.ibgs.studyAssistant.question.domain.Question;
import com.ibgs.studyAssistant.question.domain.QuestionOption;
import com.ibgs.studyAssistant.question.dto.QuestionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "options", source = "options", qualifiedByName = "mapOptions")
    QuestionResponse toResponse(Question question);

    @Named("mapOptions")
    default List<String> mapOptions(List<QuestionOption> options) {
        if (options == null || options.isEmpty()) {
            return Collections.emptyList();
        }
        return options.stream()
                .map(QuestionOption::getOptions)
                .collect(Collectors.toList());
    }

    List<QuestionResponse> toResponse(List<Question> questions);
}