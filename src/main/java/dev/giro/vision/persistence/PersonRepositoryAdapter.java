package dev.giro.vision.persistence;

import dev.giro.vision.face.domain.Person;
import dev.giro.vision.face.port.PersonRepository;
import dev.giro.vision.persistence.entity.PersonEntity;
import dev.giro.vision.persistence.jpa.PersonJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PersonRepositoryAdapter implements PersonRepository {

    private final PersonJpaRepository jpa;

    public PersonRepositoryAdapter(PersonJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Person save(Person person) {
        jpa.save(PersonEntity.from(person));
        return person;
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return jpa.findById(id).map(PersonEntity::toDomain);
    }

    @Override
    public List<Person> findAll() {
        return jpa.findAll().stream().map(PersonEntity::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
