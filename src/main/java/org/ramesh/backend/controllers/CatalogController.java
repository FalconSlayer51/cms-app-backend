package org.ramesh.backend.controllers;

import org.ramesh.backend.domain.dto.LessonCatalogResponse;
import org.ramesh.backend.domain.dto.ProgramCatalogResponse;
import org.ramesh.backend.domain.dto.TermCatalogResponse;
import org.ramesh.backend.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/programs")
    public List<ProgramCatalogResponse> programs() {
        return catalogService.listPrograms();
    }

    @GetMapping("/programs/{id}")
    public ProgramCatalogResponse program(@PathVariable UUID id) {
        return catalogService.getProgram(id);
    }

    @GetMapping("/programs/{id}/terms")
    public List<TermCatalogResponse> terms(@PathVariable UUID id) {
        return catalogService.listTerms(id);
    }

    @GetMapping("/terms/{id}/lessons")
    public List<LessonCatalogResponse> lessons(@PathVariable UUID id) {
        return catalogService.listLessons(id);
    }
}
