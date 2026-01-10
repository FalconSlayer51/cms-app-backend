package org.ramesh.backend.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.ramesh.backend.domain.enums.ContentType;
import org.ramesh.backend.domain.enums.LessonStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "lessons",
        uniqueConstraints = @UniqueConstraint(columnNames = {"term_id", "lesson_number"})
)
public class Lesson {
    @Id
    private UUID id;
    @Column(name = "term_id", nullable = false)
    private UUID termId;
    @Column(name = "lesson_number", nullable = false)
    private int lessonNumber;
    @Column(nullable = false)
    private String title;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;
    private Long durationMs;
    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;
    @Column(name = "content_language_primary", nullable = false)
    private String contentLanguagePrimary;
    @ElementCollection
    @CollectionTable(
            name = "lesson_content_languages",
            joinColumns = @JoinColumn(name = "lesson_id")
    )
    @Column(name = "language", nullable = false)
    private List<String> contentLanguagesAvailable;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content_urls_by_language", columnDefinition = "jsonb")
    private String contentUrlsByLanguage;

    @ElementCollection
    @CollectionTable(
            name = "lesson_subtitle_languages",
            joinColumns = @JoinColumn(name = "lesson_id")
    )
    @Column(name = "language")
    private List<String> subtitleLanguages;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "subtitle_urls_by_language", columnDefinition = "jsonb")
    private String subtitleUrlsByLanguage;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    private OffsetDateTime publishAt;
    private OffsetDateTime publishedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}

