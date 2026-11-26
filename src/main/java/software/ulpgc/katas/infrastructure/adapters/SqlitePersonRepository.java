package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqlitePersonRepository implements PersonRepository {

    private final Path databasePath;

    public SqlitePersonRepository(Path databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public List<Person> findAll() {
        if (!Files.isRegularFile(databasePath)) {
            throw new IllegalStateException("No existe la base de datos: " + databasePath);
        }

        String url = "jdbc:sqlite:" + databasePath.toAbsolutePath();
        String sql = "SELECT id, name, birthday FROM people";

        List<Person> people = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                people.add(new Person(
                        result.getInt("id"),
                        result.getString("name"),
                        LocalDate.parse(result.getString("birthday"))
                ));
            }

            return people;

        } catch (SQLException e) {
            throw new IllegalStateException("Error al consultar SQLite", e);
        }
    }
}