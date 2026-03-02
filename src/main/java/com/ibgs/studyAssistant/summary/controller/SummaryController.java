package com.ibgs.studyAssistant.summary.controller;

import com.ibgs.studyAssistant.summary.domain.Summary;
import com.ibgs.studyAssistant.summary.dto.SummaryTitleDTO;
import com.ibgs.studyAssistant.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/summaries")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/{id}")
    public ResponseEntity<Summary> findById(@PathVariable UUID id) {
        Summary summary = summaryService.findById(id);

        return ResponseEntity.ok(summary);
    }

    @GetMapping
    public ResponseEntity<List<SummaryTitleDTO>> findAllByUser() {
        List<SummaryTitleDTO> summary = summaryService.findAllByUser();

        return ResponseEntity.ok(summary);
    }

    @PostMapping("/generate")
    public ResponseEntity<Summary> generateSummary(@RequestBody String prompt) {
        Summary summary = summaryService.generateSumarry(prompt);

        return ResponseEntity.status(HttpStatus.CREATED).body(summary);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        summaryService.delete(id);
    }
}
