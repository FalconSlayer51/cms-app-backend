package org.ramesh.backend.service;

import jakarta.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.ramesh.backend.domain.enums.LessonStatus;
import org.ramesh.backend.repository.LessonRepository;
import org.ramesh.backend.repository.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

@Service
@EnableScheduling
public class PublishingWorkerService {
    private final LessonRepository lessonRepository;
    private final ProgramRepository programRepository;

    private static final Logger log =
            LoggerFactory.getLogger(PublishingWorkerService.class);

    public PublishingWorkerService(LessonRepository lessonRepository, ProgramRepository programRepository) {
        this.lessonRepository = lessonRepository;
        this.programRepository = programRepository;
    }

    @Transactional
    public void runOnce() {

        OffsetDateTime now = OffsetDateTime.now();


        log.info("Publishing worker tick at {}"+ now);

        int publishedLessons = lessonRepository.publishDueLessons(now, LessonStatus.scheduled, LessonStatus.published);
        if (publishedLessons == 0)
            return;

        List<UUID> programIds = lessonRepository.findProgramsWithPublishedLessons(now);
        for (var programId : programIds) {
            programRepository.publishProgramIfNeeded(programId, now);
        }
    }
}
