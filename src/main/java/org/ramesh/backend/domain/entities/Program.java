package org.ramesh.backend.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.ramesh.backend.domain.enums.ProgramStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "programs")
public class Program {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "language_primary", nullable = false)
    private String languagePrimary;

    @ElementCollection
    @CollectionTable(
            name = "program_languages",
            joinColumns = @JoinColumn(name = "program_id")
    )
    @Column(name = "language", nullable = false)
    private List<String> languagesAvailable;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)  // Add this line
    @Column(columnDefinition = "program_status")
    private ProgramStatus status;

    private OffsetDateTime publishedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
