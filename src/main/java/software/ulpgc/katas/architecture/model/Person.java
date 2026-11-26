package software.ulpgc.katas.architecture.model;

import java.time.LocalDate;

public record Person(int id, String name, LocalDate birthday) {
}
