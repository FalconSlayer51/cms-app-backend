package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.Lesson;
import org.ramesh.backend.domain.enums.LessonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    @Modifying
    @Query(
            value = """
            UPDATE lessons
            SET status = 'published',
                published_at = :now
            WHERE status = 'scheduled'
              AND publish_at <= :now
        """,
            nativeQuery = true
    )
    int publishDueLessons(
            @Param("now") OffsetDateTime now,
            @Param("scheduledStatus") LessonStatus scheduledStatus,
            @Param("publishedStatus") LessonStatus publishedStatus
    );

    @Query(
            value = """
            SELECT DISTINCT t.program_id
            FROM lessons l
            JOIN terms t ON t.id = l.term_id
            WHERE l.status = 'published'
              AND l.published_at = :now
        """,
            nativeQuery = true
    )
    List<UUID> findProgramsWithPublishedLessons(@Param("now") OffsetDateTime now);
}
