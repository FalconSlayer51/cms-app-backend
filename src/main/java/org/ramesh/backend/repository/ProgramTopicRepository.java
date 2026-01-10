package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.ProgramTopic;
import org.ramesh.backend.domain.entities.ProgramTopicId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramTopicRepository
        extends JpaRepository<ProgramTopic, ProgramTopicId> {
}
