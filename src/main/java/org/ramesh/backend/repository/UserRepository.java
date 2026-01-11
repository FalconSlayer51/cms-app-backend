package org.ramesh.backend.repository;

import org.ramesh.backend.domain.entities.User;
import org.ramesh.backend.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
    boolean existsByRole(Role role);
}
