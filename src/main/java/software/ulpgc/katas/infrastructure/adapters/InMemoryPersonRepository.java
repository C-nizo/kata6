package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPersonRepository implements PersonRepository {
    private final Map<Integer, Person> people = new LinkedHashMap<>();

    public InMemoryPersonRepository() {
        insert(new Person(1, "Ana García", LocalDate.of(1998, 4, 12)));
        insert(new Person(2, "Carlos Pérez", LocalDate.of(2001, 9, 25)));
        insert(new Person(3, "Lucía Martín", LocalDate.of(1995, 2, 8)));
    }

    @Override
    public Optional<Person> findById(int id) {
        return Optional.ofNullable(people.get(id));
    }

    @Override
    public void insert(Person person) {
        if (people.putIfAbsent(person.id(), person) != null) {
            throw new IllegalStateException("Ya existe una persona con id " + person.id());
        }
    }

    @Override
    public void update(Person person) {
        people.replace(person.id(), person);
    }

    @Override
    public void deleteById(int id) {
        people.remove(id);
    }
}
