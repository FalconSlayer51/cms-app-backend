package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.ProgramAsset;
import org.ramesh.backend.domain.enums.AssetType;
import org.ramesh.backend.domain.enums.AssetVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgramAssetRepository
        extends JpaRepository<ProgramAsset, UUID> {
    boolean existsByProgramIdAndLanguageAndVariantAndAssetType(
            UUID programId,
            String language,
            AssetVariant variant,
            AssetType assetType
    );

    List<ProgramAsset> findProgramAssetByProgramId(UUID programId);
}

