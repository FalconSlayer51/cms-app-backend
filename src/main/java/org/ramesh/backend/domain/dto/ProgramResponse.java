package org.ramesh.backend.domain.dto;

import org.ramesh.backend.domain.enums.ProgramStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProgramResponse(
        UUID id,
        String title,
        String description,
        ProgramStatus status,
        OffsetDateTime publishedAt,
        OffsetDateTime updatedAt,
        OffsetDateTime createdAt
) {

}
