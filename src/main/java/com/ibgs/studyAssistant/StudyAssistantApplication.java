package com.ibgs.studyAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StudyAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyAssistantApplication.class, args);
	}

}
