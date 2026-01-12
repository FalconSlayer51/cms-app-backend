package org.ramesh.backend.service;

import jakarta.transaction.Transactional;
import org.ramesh.backend.domain.dto.CreateProgramRequest;
import org.ramesh.backend.domain.dto.ProgramResponse;
import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.enums.ProgramStatus;
import org.ramesh.backend.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProgramService {
    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public List<ProgramResponse> list() {
        List<Program> programs = programRepository.findAll();
        return programs.stream().map(this::toResponse).toList();
    }

    public ProgramResponse create(CreateProgramRequest request) {
        validateLanguages(request);
        Program program = new Program();
        program.setId(UUID.randomUUID());
        program.setTitle(request.title());
        program.setDescription(request.description());
        program.setLanguagePrimary(request.languagePrimary());
        program.setLanguagesAvailable(request.languagesAvailable());
        program.setStatus(ProgramStatus.draft);
        program.setCreatedAt(OffsetDateTime.now());

        programRepository.save(program);
        return toResponse(program);
    }

    public ProgramResponse update(UUID id, CreateProgramRequest req) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));
        if (program.getStatus() == ProgramStatus.published) {
            throw new IllegalArgumentException("Published program cannot be edited");
        }

        validateLanguages(req);
        program.setTitle(req.title());
        program.setDescription(req.description());
        program.setLanguagesAvailable(req.languagesAvailable());
        return toResponse(program);
    }

    public void schedule(UUID id, OffsetDateTime publishAt) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));

        if (publishAt.isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date should be in future");
        }

        program.setStatus(ProgramStatus.scheduled);
        program.setPublishedAt(publishAt);
        program.setUpdatedAt(OffsetDateTime.now());
    }

    private void validateLanguages(CreateProgramRequest req) {
        if (!req.languagesAvailable().contains(req.languagePrimary())) {
            throw new IllegalArgumentException("Primary Languages must be in available languages");
        }
    }

    private ProgramResponse toResponse(Program p) {
        return new ProgramResponse(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getStatus(),
                p.getPublishedAt()
        );
    }
}
