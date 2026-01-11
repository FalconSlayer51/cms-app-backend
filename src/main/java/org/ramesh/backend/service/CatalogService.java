package org.ramesh.backend.service;

import org.ramesh.backend.domain.dto.LessonCatalogResponse;
import org.ramesh.backend.domain.dto.ProgramCatalogResponse;
import org.ramesh.backend.domain.dto.TermCatalogResponse;
import org.ramesh.backend.domain.entities.Lesson;
import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.entities.Term;
import org.ramesh.backend.domain.enums.LessonStatus;
import org.ramesh.backend.domain.enums.ProgramStatus;
import org.ramesh.backend.repository.LessonRepository;
import org.ramesh.backend.repository.ProgramRepository;
import org.ramesh.backend.repository.TermRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CatalogService {

    private final ProgramRepository programRepository;
    private final TermRepository termRepository;
    private final LessonRepository lessonRepository;

    public CatalogService(ProgramRepository programRepository, TermRepository termRepository, LessonRepository lessonRepository) {
        this.programRepository = programRepository;
        this.termRepository = termRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<ProgramCatalogResponse> listPrograms() {
        return programRepository
                .findByStatusOrderByPublishedAtDesc(ProgramStatus.published)
                .stream()
                .map(this::toProgram)
                .toList();
    }

    public ProgramCatalogResponse getProgram(UUID id) {
        Program p = programRepository.findById(id)
                .filter(pr -> pr.getStatus() == ProgramStatus.published)
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));
        return toProgram(p);
    }

    public List<TermCatalogResponse> listTerms(UUID programId) {
        return termRepository.findByProgramIdOrderByTermNumber(programId)
                .stream()
                .map(this::toTerm)
                .toList();
    }

    public List<LessonCatalogResponse> listLessons(UUID termId) {
        return lessonRepository
                .findByTermIdAndStatusOrderByLessonNumber(
                        termId, LessonStatus.published)
                .stream()
                .map(this::toLesson)
                .toList();
    }

    private ProgramCatalogResponse toProgram(Program p) {
        return new ProgramCatalogResponse(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getPublishedAt()
        );
    }

    private TermCatalogResponse toTerm(Term t) {
        return new TermCatalogResponse(
                t.getId(),
                t.getTermNumber(),
                t.getTitle()
        );
    }

    private LessonCatalogResponse toLesson(Lesson l) {
        return new LessonCatalogResponse(
                l.getId(),
                l.getLessonNumber(),
                l.getTitle(),
                l.isPaid(),
                l.getPublishedAt()
        );
    }
}
