package org.ramesh.backend.domain.dto;

import jakarta.validation.constraints.NotNull;

public record CreateTermRequest(
        @NotNull Integer termNumber,
        String title
) {
}
