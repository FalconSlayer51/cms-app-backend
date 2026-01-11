package org.ramesh.backend.controllers;

import jakarta.validation.Valid;
import org.ramesh.backend.domain.dto.CreateTermRequest;
import org.ramesh.backend.domain.dto.TermResponse;
import org.ramesh.backend.service.TermService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cms")
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
public class TermCmsController {

    private final TermService termService;

    public TermCmsController(TermService termService) {
        this.termService = termService;
    }

    @PostMapping("/programs/{programId}/terms")
    public ResponseEntity<TermResponse> create(
            @PathVariable UUID programId,
            @Valid @RequestBody CreateTermRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(termService.create(programId, request));
    }

    @PutMapping("/terms/{termId}")
    public TermResponse update(
            @PathVariable UUID termId,
            @Valid @RequestBody CreateTermRequest request
    ) {
        return termService.update(termId, request);
    }

    @GetMapping("/programs/{programId}/terms")
    public List<TermResponse> list(@PathVariable UUID programId) {
        return termService.list(programId);
    }
}
