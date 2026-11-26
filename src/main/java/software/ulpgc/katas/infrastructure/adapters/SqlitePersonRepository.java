package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.architecture.model.Person;
import software.ulpgc.katas.architecture.ports.PersonRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqlitePersonRepository implements PersonRepository {
    private final Path databasePath;

    public SqlitePersonRepository(Path databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public List<Person> findByBirthYear(int year) {
        if (!Files.isRegularFile(databasePath)) {
            throw new IllegalStateException("No existe la base de datos: " + databasePath);
        }

        String url = "jdbc:sqlite:" + databasePath.toAbsolutePath();
        String sql = "SELECT id, name, birthday FROM people WHERE birthday >= ? AND birthday < ?";
        List<Person> people = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, LocalDate.of(year, 1, 1).toString());
            statement.setString(2, LocalDate.of(year + 1, 1, 1).toString());

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    people.add(new Person(
                            result.getInt("id"),
                            result.getString("name"),
                            LocalDate.parse(result.getString("birthday"))
                    ));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Error al consultar las personas en SQLite", exception);
        }

        return people;
    }
}
