package software.ulpgc.katas.architecture.ports;

import software.ulpgc.katas.architecture.model.Person;

import java.util.Optional;

public interface PersonRepository {
    Optional<Person> findById(int id);

    void insert(Person person);

    void update(Person person);

    void deleteById(int id);
}
