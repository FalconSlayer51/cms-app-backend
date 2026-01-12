package org.ramesh.backend.domain.dto;

import java.util.List;

public record PresignResponse(
        List<String> url
) {
}
