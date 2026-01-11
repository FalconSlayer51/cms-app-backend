package org.ramesh.backend.service;

import jakarta.transaction.Transactional;
import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.enums.AssetType;
import org.ramesh.backend.domain.enums.AssetVariant;
import org.ramesh.backend.domain.enums.ProgramStatus;
import org.ramesh.backend.repository.ProgramAssetRepository;
import org.ramesh.backend.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class ProgramPublishService {
    private final ProgramRepository programRepository;
    private final ProgramAssetRepository programAssetRepository;

    public ProgramPublishService(ProgramRepository programRepository, ProgramAssetRepository programAssetRepository) {
        this.programRepository = programRepository;
        this.programAssetRepository = programAssetRepository;
    }

    public void publish(UUID programId) {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));

        if (program.getStatus() != ProgramStatus.draft) {
            throw new IllegalStateException("Only draft programs can be published");
        }

        String lang = program.getLanguagePrimary();
        boolean hasPortrait = programAssetRepository.existsByProgramIdAndLanguageAndVariantAndAssetType(
                programId,
                lang,
                AssetVariant.portrait,
                AssetType.poster
        );

        boolean hasLandscape = programAssetRepository.existsByProgramIdAndLanguageAndVariantAndAssetType(
                programId,
                lang,
                AssetVariant.landscape,
                AssetType.poster
        );
        if (!hasPortrait || !hasLandscape) {
            throw new IllegalStateException(
                    "Primary language posters (portrait & landscape) are required"
            );
        }

        program.setStatus(ProgramStatus.published);
        program.setPublishedAt(OffsetDateTime.now());
        program.setUpdatedAt(OffsetDateTime.now());
    }
}
