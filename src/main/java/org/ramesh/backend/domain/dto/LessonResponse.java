package org.ramesh.backend.domain.dto;

import org.ramesh.backend.domain.enums.LessonStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LessonResponse(
        UUID id,
        int lessonNumber,
        String title,
        LessonStatus status,
        OffsetDateTime publishAt,
        OffsetDateTime publishedAt
) {
}
