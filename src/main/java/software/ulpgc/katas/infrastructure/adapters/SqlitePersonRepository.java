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
import java.util.Optional;

public class SqlitePersonRepository implements PersonRepository {
    private final Path databasePath;

    public SqlitePersonRepository(Path databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public Optional<Person> findById(int id) {
        String sql = "SELECT id, name, birthday FROM people WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new Person(
                            result.getInt("id"),
                            result.getString("name"),
                            LocalDate.parse(result.getString("birthday"))
                    ));
                }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("Error al consultar las personas", exception);
        }
    }

    @Override
    public void insert(Person person) {
        String sql = "INSERT INTO people (id, name, birthday) VALUES (?, ?, ?)";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, person.id());
            statement.setString(2, person.name());
            statement.setString(3, person.birthday().toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Error al insertar una persona", exception);
        }
    }

    @Override
    public void update(Person person) {
        String sql = "UPDATE people SET name = ?, birthday = ? WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.name());
            statement.setString(2, person.birthday().toString());
            statement.setInt(3, person.id());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Error al actualizar una persona", exception);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM people WHERE id = ?";

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Error al eliminar una persona", exception);
        }
    }

    private Connection openConnection() throws SQLException {
        if (!Files.isRegularFile(databasePath)) {
            throw new IllegalStateException("No existe la base de datos: " + databasePath);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath.toAbsolutePath());
    }
}
