package dev.giro.vision.face.port;

import dev.giro.vision.face.domain.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(UUID id);

    List<Person> findAll();

    void deleteById(UUID id);
}
