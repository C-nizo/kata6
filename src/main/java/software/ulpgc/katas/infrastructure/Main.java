package software.ulpgc.katas.infrastructure;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.operations.ManagePeopleOperation;
import software.ulpgc.katas.architecture.ports.PersonRepository;
import software.ulpgc.katas.infrastructure.adapters.InMemoryPersonRepository;
import software.ulpgc.katas.infrastructure.adapters.SqlitePersonRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;

public class Main {
    static void main() throws IOException {
        Path original = Path.of("db", "people.db");
        if (!Files.isRegularFile(original)) {
            throw new IllegalStateException("No existe la base de datos: " + original);
        }

        Path temporary = Files.createTempFile("kata6-", ".db");
        try {
            Files.copy(original, temporary, StandardCopyOption.REPLACE_EXISTING);

            PersonRepository repository = new SqlitePersonRepository(temporary);
            ManagePeopleOperation operation = new ManagePeopleOperation(repository);

            print("Antes de INSERT", operation.findById(100));

            operation.add(new Person(100, "María López", LocalDate.of(2000, 5, 10)));
            print("Tras INSERT", operation.findById(100));

            operation.modify(new Person(100, "María Rodríguez", LocalDate.of(2000, 5, 10)));
            print("Tras UPDATE", operation.findById(100));

            operation.remove(100);
            print("Tras DELETE", operation.findById(100));
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    private static void print(String title, Optional<Person> person) {
        System.out.println(title + ": " + person.map(Person::toString).orElse("No existe"));
    }
}
