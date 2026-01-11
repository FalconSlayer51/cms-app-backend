package org.ramesh.backend.service;

import jakarta.transaction.Transactional;
import org.ramesh.backend.domain.dto.CreateLessonRequest;
import org.ramesh.backend.domain.dto.LessonResponse;
import org.ramesh.backend.domain.entities.Lesson;
import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.entities.Term;
import org.ramesh.backend.domain.enums.*;
import org.ramesh.backend.repository.LessonAssetRepository;
import org.ramesh.backend.repository.LessonRepository;
import org.ramesh.backend.repository.ProgramRepository;
import org.ramesh.backend.repository.TermRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonAssetRepository lessonAssetRepository;
    private final TermRepository termRepository;
    private final ProgramRepository programRepository;

    public LessonService(
            LessonRepository lessonRepository,
            LessonAssetRepository lessonAssetRepository,
            TermRepository termRepository,
            ProgramRepository programRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonAssetRepository = lessonAssetRepository;
        this.termRepository = termRepository;
        this.programRepository = programRepository;
    }

    public LessonResponse create(UUID termId, CreateLessonRequest req) {
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new IllegalArgumentException("Term not found"));

        Program program = programRepository.findById(term.getProgramId())
                .orElseThrow();

        if (program.getStatus() == ProgramStatus.published) {
            throw new IllegalStateException("Cannot add lessons to published program");
        }

        validateContent(req);

        ObjectMapper objectMapper = new ObjectMapper();

        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID());
        lesson.setTermId(termId);
        lesson.setLessonNumber(req.lessonNumber());
        lesson.setTitle(req.title());
        lesson.setContentType(req.contentType());
        lesson.setDurationMs(req.durationMs());
        lesson.setPaid(req.isPaid());
        lesson.setContentLanguagePrimary(req.contentLanguagePrimary());
        lesson.setContentLanguagesAvailable(req.contentLanguagesAvailable());
        lesson.setContentUrlsByLanguage(objectMapper.writeValueAsString(req.contentUrlsByLanguage()));
        lesson.setStatus(LessonStatus.draft);
        lesson.setCreatedAt(OffsetDateTime.now());
        lesson.setUpdatedAt(OffsetDateTime.now());

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    public List<LessonResponse> list(UUID termId) {
        return lessonRepository.findByTermIdOrderByLessonNumber(termId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void schedule(UUID lessonId, OffsetDateTime publishAt) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow();

        if (publishAt.isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Publish time must be in future");
        }

        lesson.setStatus(LessonStatus.scheduled);
        lesson.setPublishAt(publishAt);
        lesson.setUpdatedAt(OffsetDateTime.now());
    }

    public void publish(Lesson lesson) {
        validatePublish(lesson);
        lesson.setStatus(LessonStatus.published);
        lesson.setPublishedAt(OffsetDateTime.now());
        lesson.setUpdatedAt(OffsetDateTime.now());
    }

    private void validateContent(CreateLessonRequest req) {
        if (!req.contentLanguagesAvailable()
                .contains(req.contentLanguagePrimary())) {
            throw new IllegalArgumentException("Primary language missing");
        }

        if (!req.contentUrlsByLanguage()
                .containsKey(req.contentLanguagePrimary())) {
            throw new IllegalArgumentException("Primary language URL required");
        }

        if (req.contentType() == ContentType.video && req.durationMs() == null) {
            throw new IllegalArgumentException("Video requires duration");
        }
    }

    private void validatePublish(Lesson lesson) {
        String lang = lesson.getContentLanguagePrimary();

        boolean portrait = lessonAssetRepository
                .existsByLessonIdAndLanguageAndVariantAndAssetType(
                        lesson.getId(), lang,
                        AssetVariant.portrait, AssetType.thumbnail);

        boolean landscape = lessonAssetRepository
                .existsByLessonIdAndLanguageAndVariantAndAssetType(
                        lesson.getId(), lang,
                        AssetVariant.landscape, AssetType.thumbnail);

        if (!portrait || !landscape) {
            throw new IllegalStateException("Required thumbnails missing");
        }
    }

    private LessonResponse toResponse(Lesson l) {
        return new LessonResponse(
                l.getId(),
                l.getLessonNumber(),
                l.getTitle(),
                l.getStatus(),
                l.getPublishAt(),
                l.getPublishedAt()
        );
    }
}
