CREATE TABLE people (
                        id INTEGER PRIMARY KEY,
                        name TEXT NOT NULL,
                        birthday TEXT NOT NULL
);

INSERT INTO people (id, name, birthday) VALUES
    (1, 'Ana García', '1998-04-12'),
    (2, 'Carlos Pérez', '2001-09-25'),
    (3, 'Lucía Martín', '1995-02-08');