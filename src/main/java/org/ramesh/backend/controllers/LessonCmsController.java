package org.ramesh.backend.controllers;

import jakarta.validation.Valid;
import org.ramesh.backend.domain.dto.CreateLessonRequest;
import org.ramesh.backend.domain.dto.LessonResponse;
import org.ramesh.backend.domain.dto.ScheduleLessonRequest;
import org.ramesh.backend.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cms")
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class LessonCmsController {

    private final LessonService lessonService;

    public LessonCmsController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("/terms/{termId}/lessons")
    public ResponseEntity<LessonResponse> create(
            @PathVariable UUID termId,
            @Valid @RequestBody CreateLessonRequest request
    ) {
        System.out.println(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.create(termId, request));
    }

    @GetMapping("/terms/{termId}/lessons")
    public List<LessonResponse> list(@PathVariable UUID termId) {
        return lessonService.list(termId);
    }

    @PostMapping("/lessons/{lessonId}/schedule")
    public ResponseEntity<Void> schedule(
            @PathVariable UUID lessonId,
            @Valid @RequestBody ScheduleLessonRequest request
    ) {
        lessonService.schedule(lessonId, request.publishAt());
        return ResponseEntity.accepted().build();
    }
}
