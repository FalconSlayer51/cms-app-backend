package org.ramesh.backend.controllers;

import org.ramesh.backend.domain.dto.CreateProgramRequest;
import org.ramesh.backend.domain.dto.ProgramResponse;
import org.ramesh.backend.service.ProgramPublishService;
import org.ramesh.backend.service.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cms/programs")
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class ProgramCmsController {
    private final ProgramService programService;
    private final ProgramPublishService programPublishService;

    public ProgramCmsController(ProgramService programService, ProgramPublishService programPublishService) {
        this.programService = programService;
        this.programPublishService = programPublishService;
    }

    @GetMapping
    public List<ProgramResponse> list() {
        return programService.list();
    }

    @PostMapping
    public ResponseEntity<ProgramResponse> create(
            @RequestBody CreateProgramRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(programService.create(request));
    }

    @PutMapping("/{id}")
    public ProgramResponse update(
            @PathVariable UUID id,
            @RequestBody CreateProgramRequest request
    ) {
        return programService.update(id, request);
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<Void> schedule(
            @PathVariable UUID id,
            @RequestParam OffsetDateTime publishAt
    ) {
        programService.schedule(id, publishAt);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable UUID id) {
        try {
            programPublishService.publish(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
