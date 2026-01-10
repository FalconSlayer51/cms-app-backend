package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.LessonAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LessonAssetRepository
        extends JpaRepository<LessonAsset, UUID> {
}

