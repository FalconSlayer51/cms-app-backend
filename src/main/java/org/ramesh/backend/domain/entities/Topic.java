package org.ramesh.backend.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "topics")
public class Topic {
    @Id
    private UUID id;

    @Column
    private String name;
}
