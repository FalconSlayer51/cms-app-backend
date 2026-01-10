package org.ramesh.backend.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "terms",
    uniqueConstraints = @UniqueConstraint(
            columnNames = {"program_id", "term_number"}
    )
)
public class Term {
    @Id
    private UUID id;

    @Column(name = "program_id", nullable = false)
    private UUID programId;

    @Column(name = "term_number", nullable = false)
    private int termNumber;

    private String title;
    private OffsetDateTime createdAt;
}
