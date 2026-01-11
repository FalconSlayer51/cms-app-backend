package org.ramesh.backend.domain.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LessonCatalogResponse(
        UUID id,
        int lessonNumber,
        String title,
        boolean isPaid,
        OffsetDateTime publishedAt
) {
}
