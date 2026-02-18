package com.ibgs.studyAssistant.studyCalendar.dto.studyDay;

public record StudyDayDescriptionRequest(String description) {

    public String description() {
        return (description == null || description.isBlank())
                ? "Estudo sem título"
                : description;
    }
}