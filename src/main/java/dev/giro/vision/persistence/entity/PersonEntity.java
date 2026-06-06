package dev.giro.vision.persistence.entity;

import dev.giro.vision.face.domain.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class PersonEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected PersonEntity() {}

    public static PersonEntity from(Person person) {
        PersonEntity e = new PersonEntity();
        e.id = person.id();
        e.name = person.name();
        e.createdAt = person.createdAt();
        return e;
    }

    public Person toDomain() {
        return new Person(id, name, createdAt);
    }

    public UUID getId() { return id; }
}
