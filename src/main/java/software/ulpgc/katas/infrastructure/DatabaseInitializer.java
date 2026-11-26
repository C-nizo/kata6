package software.ulpgc.katas.infrastructure;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    static void main() throws Exception {
        Path databaseDirectory = Path.of("db");
        Files.createDirectories(databaseDirectory);

        String url = "jdbc:sqlite:db/people.db";

        try (InputStream input = DatabaseInitializer.class.getResourceAsStream("/people.sql")) {
            if (input == null) {
                throw new IllegalStateException("No se encuentra people.sql");
            }

            String sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            try (Connection connection = DriverManager.getConnection(url);
                 Statement statement = connection.createStatement()) {

                for (String instruction : sql.split(";")) {
                    if (!instruction.isBlank()) {
                        statement.execute(instruction);
                    }
                }
            }
        }

        System.out.println("Base de datos inicializada correctamente.");
    }
}