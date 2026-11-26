package software.ulpgc.katas.architecture.ports;

import software.ulpgc.katas.architecture.model.Person;

import java.util.List;

public interface PersonRepository {
    List<Person> findAll();
}
