package software.ulpgc.katas.infrastructure;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.operations.ListPeopleOperation;
import software.ulpgc.katas.architecture.ports.PersonRepository;
import software.ulpgc.katas.infrastructure.adapters.SqlitePersonRepository;

import java.nio.file.Path;

public class Main {

    static void main() {
        Path databasePath = Path.of("db/people.db");

        PersonRepository repository = new SqlitePersonRepository(databasePath);

        ListPeopleOperation operation = new ListPeopleOperation(repository);

        for (Person person : operation.execute()) {
            System.out.println(person);
        }
    }
}