package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.Program;
import org.ramesh.backend.domain.enums.ProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {
    @Modifying
    @Query("""
            UPDATE Program p
               SET p.status = 'published', p.publishedAt = :now
            WHERE p.id = :programId
            AND p.status <> 'published'
            """
    )
    int publishProgramIfNeeded(
            @Param("programId") UUID programId,
            @Param("now") OffsetDateTime now
    );

    List<Program> findByStatusOrderByPublishedAtDesc(ProgramStatus status);

    @Query("""
                SELECT p FROM Program p
                WHERE p.status = 'scheduled' AND p.publishedAt <= :now
            """)
    List<Program> findScheduledProgramsDue(@Param("now") OffsetDateTime now);
}
