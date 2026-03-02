package com.ibgs.studyAssistant.resume.controller;

import com.ibgs.studyAssistant.resume.domain.Resume;
import com.ibgs.studyAssistant.resume.dto.ResumeTitleDTO;
import com.ibgs.studyAssistant.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/{id}")
    public ResponseEntity<Resume> findById(@PathVariable UUID id) {
        Resume resume = resumeService.findById(id);

        return ResponseEntity.ok(resume);
    }

    @GetMapping
    public ResponseEntity<List<ResumeTitleDTO>> findAllByUser() {
        List<ResumeTitleDTO> resume = resumeService.findAllByUser();

        return ResponseEntity.ok(resume);
    }

    @PostMapping("/generate")
    public ResponseEntity<Resume> generateResume(@RequestBody String prompt) {
        Resume resume = resumeService.generateResume(prompt);

        return ResponseEntity.status(HttpStatus.CREATED).body(resume);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        resumeService.delete(id);
    }
}
