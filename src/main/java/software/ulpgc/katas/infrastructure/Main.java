package software.ulpgc.katas.infrastructure;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.operations.ListPeopleBornInYearOperation;
import software.ulpgc.katas.architecture.ports.PersonRepository;
import software.ulpgc.katas.infrastructure.adapters.InMemoryPersonRepository;
import software.ulpgc.katas.infrastructure.adapters.SqlitePersonRepository;

import java.nio.file.Path;

public class Main {
    static void main() {
        Path databasePath = Path.of("db", "people.db");
        PersonRepository repository = new SqlitePersonRepository(databasePath);
        ListPeopleBornInYearOperation operation = new ListPeopleBornInYearOperation(repository);

        int year = 1998;
        for (Person person : operation.execute(year)) {
            System.out.println(person);
        }
    }
}
