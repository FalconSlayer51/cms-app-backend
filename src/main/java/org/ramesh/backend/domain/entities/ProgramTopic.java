package org.ramesh.backend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "program_topics")
@IdClass(ProgramTopicId.class)
public class ProgramTopic {
    @Id
    private UUID programId;
    @Id
    private UUID topicId;
}

