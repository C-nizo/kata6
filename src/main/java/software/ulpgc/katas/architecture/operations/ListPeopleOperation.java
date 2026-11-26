package software.ulpgc.katas.architecture.operations;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.util.List;

public class ListPeopleOperation {
    private final PersonRepository repository;

    public ListPeopleOperation(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> execute() {
        return repository.findAll();
    }
}
