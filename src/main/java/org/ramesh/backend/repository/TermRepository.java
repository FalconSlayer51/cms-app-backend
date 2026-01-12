package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TermRepository extends JpaRepository<Term, UUID> {
    List<Term> findByProgramIdOrderByTermNumber(UUID programId);

    Term findByProgramId(UUID programId);
}
