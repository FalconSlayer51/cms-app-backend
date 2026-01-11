package org.ramesh.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.ramesh.backend.domain.enums.ContentType;

import java.util.List;
import java.util.Map;

public record CreateLessonRequest(
        @NotNull Integer lessonNumber,
        @NotBlank String title,
        @NotNull ContentType contentType,
        Long durationMs,
        boolean isPaid,

        @NotBlank String contentLanguagePrimary,
        @NotNull List<String> contentLanguagesAvailable,
        @NotNull Map<String, String> contentUrlsByLanguage
) {
}
