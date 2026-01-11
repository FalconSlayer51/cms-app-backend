package org.ramesh.backend.domain.dto;


public record PresignAssetResponse(
        String uploadUrl,
        String publicUrl
) {
}
