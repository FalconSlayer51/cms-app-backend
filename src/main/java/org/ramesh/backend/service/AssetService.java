package org.ramesh.backend.service;

import org.ramesh.backend.domain.dto.PresignAssetRequest;
import org.ramesh.backend.domain.dto.PresignAssetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Duration;
import java.util.UUID;

@Service
public class AssetService {
    private final S3Presigner preSigner;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Value("${CDN_BASE_URL}")
    private String cdnBaseUrl;

    public AssetService(S3Presigner preSigner) {
        this.preSigner = preSigner;
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
                .contentType("image/jpeg")
                .build();

        String uploadUrl = preSigner.presignPutObject(r -> r
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
        ).url().toString();

        String publicUrl = cdnBaseUrl + "/" + key;

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
                .contentType("image/jpeg")
                .build();

        String uploadUrl = preSigner.presignPutObject(r -> r
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putReq)
        ).url().toString();

        String publicUrl = cdnBaseUrl + "/" + key;

        return new PresignAssetResponse(uploadUrl, publicUrl);
    }

}
