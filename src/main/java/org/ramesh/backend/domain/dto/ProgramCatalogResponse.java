package org.ramesh.backend.domain.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProgramCatalogResponse(
        UUID id,
        String title,
        String description,
        OffsetDateTime publishedAt
) {
}
