package org.ramesh.backend.domain.entities;


import jakarta.persistence.*;
import org.ramesh.backend.domain.enums.AssetType;
import org.ramesh.backend.domain.enums.AssetVariant;

import java.util.UUID;

@Entity
@Table(
        name = "program_assets",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"program_id", "language", "variant", "asset_type"}
        )
)
public class ProgramAsset {

    @Id
    private UUID id;

    @Column(name = "program_id", nullable = false)
    private UUID programId;

    @Column(nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    private AssetVariant variant;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type")
    private AssetType assetType;

    @Column(nullable = false)
    private String url;
}
