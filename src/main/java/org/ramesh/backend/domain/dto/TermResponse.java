package org.ramesh.backend.domain.dto;

import java.util.UUID;

public record TermResponse(
        UUID id,
        Integer termNumber,
        String title
) {
}
