package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.time.LocalDate;
import java.util.List;

public class InMemoryPersonRepository implements PersonRepository {
    private final List<Person> people = List.of(
            new Person(1, "Ana García", LocalDate.of(1998, 4, 12)),
            new Person(2, "Carlos Pérez", LocalDate.of(2001, 9, 25)),
            new Person(3, "Lucía Martín", LocalDate.of(1995, 2, 8))
    );

    @Override
    public List<Person> findByBirthYear(int year) {
        return people.stream()
                .filter(person -> person.birthday().getYear() == year)
                .toList();
    }
}
