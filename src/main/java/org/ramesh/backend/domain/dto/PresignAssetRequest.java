package org.ramesh.backend.domain.dto;

import org.ramesh.backend.domain.enums.AssetType;
import org.ramesh.backend.domain.enums.AssetVariant;

public record PresignAssetRequest(
        AssetType assetType,
        AssetVariant variant,
        String language,
        String fileName,
        String contentType
) {}
