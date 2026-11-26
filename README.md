# Kata 6 — Persistencia con SQLite y JDBC

Práctica de Ingeniería del Software 2 (ULPGC). El proyecto permite recuperar objetos Java desde una base de datos SQLite mediante JDBC, separando el acceso a datos del resto de la aplicación mediante un puerto y adaptadores intercambiables.

La tercera repetición amplía el ejemplo con las operaciones SQL básicas de consulta, inserción, actualización y eliminación.

Repositorio: [C-nizo/kata6](https://github.com/C-nizo/kata6).

## Requisitos y dependencias

- Java 27.
- Maven e IntelliJ IDEA (o un IDE compatible con Maven).
- SQLite, como base de datos almacenada en un archivo local.
- SQLite JDBC 3.54.1.0 (`org.xerial:sqlite-jdbc`), para acceder a SQLite desde Java.

La dependencia JDBC está declarada en `pom.xml`. No es necesario instalar el ejecutable `sqlite3`, desplegar un servidor de bases de datos ni utilizar Docker.

## Arquitectura y estructura

El proyecto utiliza una separación de responsabilidades basada en arquitectura hexagonal:

| Componente | Responsabilidad |
| --- | --- |
| `Person` | Modelo de persona (`id`, `name` y `birthday`). |
| `PersonRepository` | Puerto que define las operaciones sobre personas sin depender de SQLite. |
| `ManagePeopleOperation` | Expone las operaciones de consulta, alta, modificación y baja mediante el puerto. |
| `SqlitePersonRepository` | Adaptador que ejecuta las sentencias SQL con JDBC y transforma los resultados en objetos `Person`. |
| `InMemoryPersonRepository` | Adaptador alternativo que mantiene las personas en memoria. |
| `DatabaseInitializer` | Crea la base de datos local y ejecuta el script de inicialización. |
| `Main` | Construye los componentes y demuestra las operaciones sobre una copia temporal de la base de datos. |
| `src/main/resources/people.sql` | Define la tabla `people` e inserta tres registros de ejemplo. |

El modelo, el puerto y las operaciones no dependen de JDBC. Ambos adaptadores implementan el mismo contrato y pueden intercambiarse sin modificar el caso de uso.

## Compilación y ejecución

### 1. Obtener y compilar el proyecto

Clona el repositorio:

```bash
git clone https://github.com/C-nizo/kata6.git
cd kata6
```

Abre el proyecto como proyecto Maven en IntelliJ IDEA, selecciona el JDK 27 y espera a que Maven descargue las dependencias. Después utiliza **Build → Build Project**.

También puede compilarse desde una terminal que tenga Maven instalado:

```bash
mvn clean compile
```

### 2. Inicializar SQLite

Ejecuta una vez desde IntelliJ la clase:

```text
software.ulpgc.katas.infrastructure.DatabaseInitializer
```

La inicialización lee `src/main/resources/people.sql`, crea el directorio `db` y genera el archivo `db/people.db` con tres personas: Ana García, Carlos Pérez y Lucía Martín.

Salida esperada:

```text
Base de datos inicializada correctamente.
```

**Importante:** ejecuta el inicializador solo cuando `db/people.db` todavía no exista. El script contiene `CREATE TABLE people` y no está preparado para repetirse sobre una tabla ya creada. Si necesitas reconstruir los datos desde cero, elimina primero la base de datos local, teniendo en cuenta que se perderán los cambios que contenga.

El archivo `.db` está excluido de Git mediante `.gitignore`; se genera en cada equipo a partir del script SQL.

### 3. Ejecutar la aplicación

Ejecuta desde IntelliJ:

```text
software.ulpgc.katas.infrastructure.Main
```

El directorio de trabajo debe ser la raíz del proyecto, porque las rutas `db/people.db` son relativas a ella.

La aplicación crea una copia temporal de la base de datos, realiza las cuatro operaciones sobre la persona de identificador `100` y elimina la copia al finalizar. La base de datos original permanece intacta.

## Operaciones SQL

En la tercera repetición, el adaptador SQLite utiliza `PreparedStatement` y parámetros (`?`) para las cuatro sentencias:

| Operación | Sentencia |
| --- | --- |
| Consultar por identificador | `SELECT id, name, birthday FROM people WHERE id = ?` |
| Insertar | `INSERT INTO people (id, name, birthday) VALUES (?, ?, ?)` |
| Actualizar | `UPDATE people SET name = ?, birthday = ? WHERE id = ?` |
| Eliminar | `DELETE FROM people WHERE id = ?` |

Las consultas utilizan `executeQuery()` y leen un `ResultSet`; las escrituras utilizan `executeUpdate()`. Las conexiones y sentencias se cierran mediante `try-with-resources` y los errores SQL se encapsulan en excepciones de la aplicación. La búsqueda devuelve `Optional<Person>` cuando una persona puede no existir.

## Verificación

Tras inicializar la base de datos y ejecutar `Main`, debe aparecer:

```text
Antes de INSERT: No existe
Tras INSERT: Person[id=100, name=María López, birthday=2000-05-10]
Tras UPDATE: Person[id=100, name=María Rodríguez, birthday=2000-05-10]
Tras DELETE: No existe
```

Para comprobar la sustitución de adaptadores, cambia temporalmente en `Main`:

```java
PersonRepository repository = new SqlitePersonRepository(temporary);
```

por:

```java
PersonRepository repository = new InMemoryPersonRepository();
```

Con el import correspondiente, la salida de las operaciones debe ser la misma. Después, restaura el adaptador SQLite.

La aplicación también comprueba que existe el archivo de base de datos antes de acceder a él, evitando que SQLite cree silenciosamente una base de datos vacía si la ruta es incorrecta.

Estas comprobaciones se realizaron manualmente desde IntelliJ IDEA; el proyecto no incorpora pruebas JUnit.

## Repeticiones

La actividad repetida fue implementar la lectura de registros SQLite desde Java mediante JDBC, mapear el `ResultSet` a objetos y consumir el resultado a través de un puerto. En cada repetición se rehízo la implementación, conservando la inicialización y el script de la base de datos.

1. **Primera repetición:** `findAll()` con una sentencia `SELECT` que recupera todas las personas.
2. **Segunda repetición:** `findByBirthYear(int year)` con `PreparedStatement` y una consulta parametrizada por intervalo de fechas.
3. **Tercera repetición (versión final):** `findById(int id)` con `Optional<Person>`, junto con `INSERT`, `UPDATE` y `DELETE` para practicar operaciones de escritura. La demostración utiliza una copia temporal de la base de datos.

Las tres implementaciones y los commits de preparación se conservan en el historial Git. Para consultarlos:

```bash
git log --oneline
```

Por ejemplo, puede mostrarse el archivo `Main.java` de cada repetición sin cambiar de rama:

```bash
git show fc04d705:src/main/java/software/ulpgc/katas/infrastructure/Main.java
git show 6496b5fc:src/main/java/software/ulpgc/katas/infrastructure/Main.java
git show d0a83f31:src/main/java/software/ulpgc/katas/infrastructure/Main.java
```

La rama `master` contiene la tercera repetición, que constituye la versión final evaluable; `develop` se utiliza como rama de trabajo.

## Vídeo explicativo

**Pendiente de grabación.** Se incorporará aquí el enlace al vídeo explicativo (duración máxima de 7 minutos) mediante un commit posterior.