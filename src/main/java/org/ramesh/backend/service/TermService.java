package org.ramesh.backend.service;

import jakarta.transaction.Transactional;
import org.ramesh.backend.domain.dto.CreateTermRequest;
import org.ramesh.backend.domain.dto.TermResponse;
import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.entities.Term;
import org.ramesh.backend.domain.enums.ProgramStatus;
import org.ramesh.backend.repository.ProgramRepository;
import org.ramesh.backend.repository.TermRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TermService {

    private final TermRepository termRepository;
    private final ProgramRepository programRepository;

    public TermService(TermRepository termRepository, ProgramRepository programRepository) {
        this.termRepository = termRepository;
        this.programRepository = programRepository;
    }

    public TermResponse create(UUID programId, CreateTermRequest request) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));

        if (program.getStatus() == ProgramStatus.published) {
            throw new IllegalStateException("Cannot add terms to a published program");
        }

        Term term = new Term();
        term.setId(UUID.randomUUID());
        term.setProgramId(programId);
        term.setTermNumber(request.termNumber());
        term.setTitle(request.title());
        term.setCreatedAt(OffsetDateTime.now());

        termRepository.save(term);
        return toResponse(term);
    }

    public TermResponse update(UUID termId, CreateTermRequest request) {
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new IllegalArgumentException("Term not found"));

        Program program = programRepository.findById(term.getProgramId())
                .orElseThrow();

        if (program.getStatus() == ProgramStatus.published) {
            throw new IllegalStateException("Cannot edit terms of a published program");
        }
        term.setTermNumber(request.termNumber());
        term.setTitle(request.title());

        return toResponse(term);
    }

    public List<TermResponse> list(UUID programId) {
        return termRepository.findByProgramIdOrderByTermNumber(programId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TermResponse toResponse(Term t) {
        return new TermResponse(
                t.getId(),
                t.getTermNumber(),
                t.getTitle()
        );
    }
}
