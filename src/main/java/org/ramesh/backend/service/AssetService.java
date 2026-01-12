package org.ramesh.backend.service;

import org.ramesh.backend.domain.dto.PresignAssetRequest;
import org.ramesh.backend.domain.dto.PresignAssetResponse;
import org.ramesh.backend.domain.dto.PresignResponse;
import org.ramesh.backend.domain.entities.LessonAsset;
import org.ramesh.backend.domain.entities.ProgramAsset;
import org.ramesh.backend.repository.LessonAssetRepository;
import org.ramesh.backend.repository.ProgramAssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class AssetService {
    private final S3Presigner preSigner;
    private final ProgramAssetRepository programAssetRepository;
    private final LessonAssetRepository lessonAssetRepository;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Value("${CDN_BASE_URL}")
    private String cdnBaseUrl;

    public AssetService(S3Presigner preSigner, ProgramAssetRepository programAssetRepository, LessonAssetRepository lessonAssetRepository) {
        this.preSigner = preSigner;
        this.programAssetRepository = programAssetRepository;
        this.lessonAssetRepository = lessonAssetRepository;
    }

    public PresignAssetResponse presignAssetResponse(
            UUID programId,
            PresignAssetRequest req
    ) {
        String key = "program/%s/%s/%s/%s"
                .formatted(programId, req.language(), req.variant(), req.fileName());

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(req.contentType())
                .build();

        String uploadUrl = preSigner.presignPutObject(r -> r
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
        ).url().toString();

        String publicUrl = cdnBaseUrl + "/" + key;
        ProgramAsset asset = new ProgramAsset();
        asset.setId(UUID.randomUUID());
        asset.setProgramId(programId);
        asset.setUrl(publicUrl);
        asset.setAssetType(req.assetType());
        asset.setLanguage(req.language());
        asset.setVariant(req.variant());

        programAssetRepository.save(asset);


        return new PresignAssetResponse(uploadUrl, publicUrl);
    }

    public PresignAssetResponse presignLessonAssetResponse(
            UUID lessonId,
            PresignAssetRequest req
    ) {
        String key = "lessons/%s/%s/%s/%s"
                .formatted(
                        lessonId,
                        req.language(),
                        req.variant(),
                        req.fileName()
                );

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(req.contentType())
                .build();

        String uploadUrl = preSigner.presignPutObject(r -> r
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
        ).url().toString();

        String publicUrl = cdnBaseUrl + "/" + key;

        LessonAsset asset = new LessonAsset();
        asset.setId(UUID.randomUUID());
        asset.setLessonId(lessonId);
        asset.setUrl(publicUrl);
        asset.setAssetType(req.assetType());
        asset.setLanguage(req.language());
        asset.setVariant(req.variant());

        lessonAssetRepository.save(asset);

        return new PresignAssetResponse(uploadUrl, publicUrl);
    }

    public PresignResponse getAllLessonUrls(UUID lessonId) {
        List<String> urls = lessonAssetRepository.findLessonAssetByLessonId(lessonId).stream()
                .map(LessonAsset::getUrl)
                .toList();
        return new PresignResponse(urls);
    }

    public PresignResponse getAllProgramUrls(UUID programId) {
        List<String> urls = programAssetRepository.findProgramAssetByProgramId(programId).stream()
                .map(ProgramAsset::getUrl)
                .toList();
        return new PresignResponse(urls);
    }
}
