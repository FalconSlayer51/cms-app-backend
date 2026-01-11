package org.ramesh.backend.controllers;

import org.ramesh.backend.domain.dto.PresignAssetRequest;
import org.ramesh.backend.domain.dto.PresignAssetResponse;
import org.ramesh.backend.service.AssetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cms/assets")
@PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
public class AssetCmsController {

    private final AssetService assetService;

    public AssetCmsController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/presign/program/{programId}")
    public PresignAssetResponse presignProgram(
            @PathVariable UUID programId,
            @RequestBody PresignAssetRequest request
    ) {
        return assetService.presignAssetResponse(programId, request);
    }

    @PostMapping("/presign/lesson/{lessonId}")
    public PresignAssetResponse presignLesson(
            @PathVariable UUID lessonId,
            @RequestBody PresignAssetRequest request
    ) {
        return assetService.presignLessonAssetResponse(lessonId, request);
    }
}

