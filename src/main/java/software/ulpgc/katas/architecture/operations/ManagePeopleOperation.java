package software.ulpgc.katas.architecture.operations;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.util.Optional;

public class ManagePeopleOperation {
    private final PersonRepository repository;

    public ManagePeopleOperation(PersonRepository repository) {
        this.repository = repository;
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public void add(Person person) {
        repository.insert(person);
    }

    public void modify(Person person) {
        repository.update(person);
    }

    public void remove(int id) {
        repository.deleteById(id);
    }
}
