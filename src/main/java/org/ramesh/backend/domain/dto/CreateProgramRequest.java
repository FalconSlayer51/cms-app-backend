package org.ramesh.backend.domain.dto;

import java.util.List;

public record CreateProgramRequest(
        String title,
        String description,
        String languagePrimary,
        List<String> languagesAvailable
) {

}
