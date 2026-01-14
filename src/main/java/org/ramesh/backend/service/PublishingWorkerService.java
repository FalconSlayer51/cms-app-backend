package org.ramesh.backend.service;

import jakarta.transaction.Transactional;
import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.enums.LessonStatus;
import org.ramesh.backend.repository.LessonRepository;
import org.ramesh.backend.repository.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.List;

@Service
@EnableScheduling
public class PublishingWorkerService {
    private final LessonRepository lessonRepository;
    private final ProgramRepository programRepository;
    private final ProgramPublishService programPublishService;

    private static final Logger log =
            LoggerFactory.getLogger(PublishingWorkerService.class);

    public PublishingWorkerService(LessonRepository lessonRepository, ProgramRepository programRepository, ProgramPublishService programPublishService) {
        this.lessonRepository = lessonRepository;
        this.programRepository = programRepository;
        this.programPublishService = programPublishService;
    }

    @Transactional
    public void runOnce() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        log.info("Publishing worker tick at {}", now);
        int publishedLessons = lessonRepository.publishDueLessons(now, LessonStatus.scheduled, LessonStatus.published);
        List<UUID> programIds = lessonRepository.findProgramsWithPublishedLessons(now);
        for (var programId : programIds) {
            programRepository.publishProgramIfNeeded(programId, now);
        }
        // NEW: Publish scheduled programs whose publishAt <= now
        var scheduledPrograms = programRepository.findScheduledProgramsDue(now);
        log.info("Found {} scheduled programs due for publishing: {}", scheduledPrograms.size(),
                scheduledPrograms.stream().map(Program::getId).toList());

        for (var program : scheduledPrograms) {
            try {
                programPublishService.publish(program.getId());
                log.info("Published scheduled program: {}", program.getId());
            } catch (Exception e) {
                log.error("Failed to publish scheduled program: {}", program.getId(), e);
            }
        }
    }
}
