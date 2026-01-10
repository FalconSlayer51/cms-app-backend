package org.ramesh.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.ramesh.backend.domain.entities.*;
import org.ramesh.backend.domain.enums.*;
import org.ramesh.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;

@Component
@Transactional
public class SeedDataRunner implements CommandLineRunner {

    private final ProgramRepository programRepository;
    private final TopicRepository topicRepository;
    private final ProgramTopicRepository programTopicRepository;
    private final TermRepository termRepository;
    private final LessonRepository lessonRepository;
    private final ProgramAssetRepository programAssetRepository;
    private final LessonAssetRepository lessonAssetRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SeedDataRunner(
            ProgramRepository programRepository,
            TopicRepository topicRepository,
            ProgramTopicRepository programTopicRepository,
            TermRepository termRepository,
            LessonRepository lessonRepository,
            ProgramAssetRepository programAssetRepository,
            LessonAssetRepository lessonAssetRepository
    ) {
        this.programRepository = programRepository;
        this.topicRepository = topicRepository;
        this.programTopicRepository = programTopicRepository;
        this.termRepository = termRepository;
        this.lessonRepository = lessonRepository;
        this.programAssetRepository = programAssetRepository;
        this.lessonAssetRepository = lessonAssetRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Sentinel check (correct idempotency)
        if (topicRepository.findByName("Java").isPresent()) {
            return;
        }

        // -------- Topics --------
        Topic java = topicRepository.save(new Topic(UUID.randomUUID(), "Java"));
        Topic spring = topicRepository.save(new Topic(UUID.randomUUID(), "Spring Boot"));

        // -------- Program 1 --------
        Program program1 = new Program();
        program1.setId(UUID.randomUUID());
        program1.setTitle("Java Backend Program");
        program1.setDescription("Learn Java and Spring Boot");
        program1.setLanguagePrimary("en");
        program1.setLanguagesAvailable(List.of("en"));
        program1.setStatus(ProgramStatus.draft);
        program1.setCreatedAt(OffsetDateTime.now());

        programRepository.save(program1);

        programTopicRepository.save(new ProgramTopic(program1.getId(), java.getId()));

        programAssetRepository.saveAll(List.of(
                new ProgramAsset(UUID.randomUUID(), program1.getId(), "en",
                        AssetVariant.portrait, AssetType.poster,
                        "https://cdn.example.com/programs/java/portrait.jpg"),
                new ProgramAsset(UUID.randomUUID(), program1.getId(), "en",
                        AssetVariant.landscape, AssetType.poster,
                        "https://cdn.example.com/programs/java/landscape.jpg")
        ));

        // -------- Term --------
        Term term1 = new Term(UUID.randomUUID(), program1.getId(), 1,
                "Foundations", OffsetDateTime.now());
        termRepository.save(term1);

        // -------- Lessons --------
        Lesson lesson1 = createLesson(term1.getId(), 1,
                "Intro to Java", LessonStatus.published,
                OffsetDateTime.now().minusDays(1));

        Lesson lesson2 = createLesson(term1.getId(), 2,
                "Spring Boot Basics", LessonStatus.scheduled,
                OffsetDateTime.now().plusMinutes(2));

        lessonRepository.saveAll(List.of(lesson1, lesson2));

        seedLessonAssets(lesson1);
        seedLessonAssets(lesson2);

        // -------- Program 2 --------
        Program program2 = new Program();
        program2.setId(UUID.randomUUID());
        program2.setTitle("Full Stack Program");
        program2.setDescription("Multi-language program");
        program2.setLanguagePrimary("en");
        program2.setLanguagesAvailable(List.of("en"));
        program2.setStatus(ProgramStatus.draft);
        program2.setCreatedAt(OffsetDateTime.now());

        programRepository.save(program2);

        programAssetRepository.saveAll(List.of(
                new ProgramAsset(UUID.randomUUID(), program2.getId(), "en",
                        AssetVariant.portrait, AssetType.poster,
                        "https://cdn.example.com/programs/fullstack/portrait.jpg"),
                new ProgramAsset(UUID.randomUUID(), program2.getId(), "en",
                        AssetVariant.landscape, AssetType.poster,
                        "https://cdn.example.com/programs/fullstack/landscape.jpg")
        ));
    }

    private Lesson createLesson(
            UUID termId,
            int number,
            String title,
            LessonStatus status,
            OffsetDateTime publishAt
    ) throws Exception {

        Map<String, String> urls = Map.of("en", "https://example.com/video.mp4");

        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID());
        lesson.setTermId(termId);
        lesson.setLessonNumber(number);
        lesson.setTitle(title);
        lesson.setContentType(ContentType.video);
        lesson.setDurationMs(600_000L);
        lesson.setPaid(false);
        lesson.setContentLanguagePrimary("en");
        lesson.setContentLanguagesAvailable(List.of("en"));
        lesson.setStatus(status);
        lesson.setPublishAt(publishAt);
        if (status == LessonStatus.published) {
            lesson.setPublishedAt(OffsetDateTime.now());
        }

        lesson.setContentUrlsByLanguage(objectMapper.writeValueAsString(urls));
        lesson.setCreatedAt(OffsetDateTime.now());

        return lesson;
    }

    private void seedLessonAssets(Lesson lesson) {
        lessonAssetRepository.saveAll(List.of(
                new LessonAsset(UUID.randomUUID(), lesson.getId(), "en",
                        AssetVariant.portrait, AssetType.thumbnail,
                        "https://cdn.example.com/lessons/thumb/portrait.jpg"),
                new LessonAsset(UUID.randomUUID(), lesson.getId(), "en",
                        AssetVariant.landscape, AssetType.thumbnail,
                        "https://cdn.example.com/lessons/thumb/landscape.jpg")
        ));
    }
}
