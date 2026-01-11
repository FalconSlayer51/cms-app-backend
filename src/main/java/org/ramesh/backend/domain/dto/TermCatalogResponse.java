package org.ramesh.backend.domain.dto;

import java.util.UUID;

public record TermCatalogResponse(
        UUID id,
        int termNumber,
        String title
) {
}
