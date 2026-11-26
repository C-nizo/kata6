package software.ulpgc.katas.architecture.operations;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.util.List;

public class ListPeopleBornInYearOperation {
    private final PersonRepository repository;

    public ListPeopleBornInYearOperation(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> execute(int year) {
        return repository.findByBirthYear(year);
    }
}
