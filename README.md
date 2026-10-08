# Dijkstra Graph App

Finds the **shortest path between two vertices of a graph** using Dijkstra's
algorithm, and also lists **every possible route** between two vertices.

A Spring Boot application that runs entirely on your machine: frontend and
backend start in the same process, and the database is in-memory. There is
nothing to install, configure or provision.

## Requirement

Just a **JDK 21 or newer**.

```bash
java -version
```

Maven is not required — the project ships with the Maven Wrapper (`./mvnw`).

## Running it

```bash
git clone https://github.com/AllanAlmeida/dijkstra-graph-app.git
cd dijkstra-graph-app
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

Then open <http://localhost:8080>.

If port 8080 is already taken:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

| Address | What it is |
|---|---|
| <http://localhost:8080> | the calculator UI |
| <http://localhost:8080/swagger-ui.html> | interactive API docs |
| <http://localhost:8080/h2-console> | database console (JDBC URL `jdbc:h2:mem:graphdb`, user `sa`, no password) |

## Sample graphs

The database is recreated on every startup and seeded with two graphs, so
there is data to work with right away:

| Id | Edges (source, target, weight) |
|---|---|
| **1** | AB4, AE8 |
| **2** | AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7 |

The graph is **directed**: `AB5` lets you go from A to B, not the other way
around. That is why there is no path from C to A in graph 2.

## API

### Shortest path

```bash
curl http://localhost:8080/distance/2/from/A/to/C
# {"distance":9,"path":["A","B","C"]}
```

A→B→C costs 5+4=9, less than A→D→C (5+8=13) or A→E→B→C (7+3+4=14).

Returns **404** when the graph does not exist, when either vertex does not
belong to it, or when there is no path between them.

### All routes

```bash
curl "http://localhost:8080/routes/2/from/A/to/C?maxStops=4"
# {"routes":[{"route":"ABC","stops":2},{"route":"ADC","stops":2},
#            {"route":"AEBC","stops":3},{"route":"ADEBC","stops":4}]}
```

`maxStops` is optional and caps the number of stops. Routes come back sorted
from shortest to longest.

### Graphs

```bash
curl http://localhost:8080/graph        # list all
curl http://localhost:8080/graph/2      # fetch by id

curl -X POST http://localhost:8080/graph \
  -H 'Content-Type: application/json' \
  -d '{"data":[{"source":"A","target":"B","distance":3},
               {"source":"B","target":"C","distance":2}]}'
```

## Tests

```bash
./mvnw test
```

21 tests: Dijkstra's algorithm on its own (`DijkstraTest`), the HTTP layer
with a live server (`GraphHttpTest`), and the controllers through MockMvc.

## Packaging

```bash
./mvnw clean package
java -jar target/dijkstra-graph-app-1.0.0.jar
```

## Stack

Java 21 · Spring Boot 4.1.1 · Spring Data JPA · in-memory H2 · Thymeleaf ·
Bootstrap 5 · springdoc-openapi

Data lives in memory: anything you create is gone when the application stops,
and the two sample graphs come back on the next startup.
