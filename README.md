# Dijkstra Graph App

Calcula o **menor caminho entre dois vértices de um grafo** com o algoritmo de
Dijkstra, e também lista **todas as rotas possíveis** entre dois vértices.

Aplicação Spring Boot que roda inteiramente na sua máquina: frontend e backend
sobem no mesmo processo, e o banco é em memória. Não há nada para instalar,
configurar ou provisionar.

## Pré-requisito

Apenas um **JDK 21 ou superior**.

```bash
java -version
```

Maven não é necessário: o projeto traz o Maven Wrapper (`./mvnw`).

## Como rodar

```bash
git clone https://github.com/AllanAlmeida/dijkstra-graph-app.git
cd dijkstra-graph-app
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd` em vez de `./mvnw`.

Depois abra <http://localhost:8080>.

Se a porta 8080 estiver ocupada:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

| Endereço | O que é |
|---|---|
| <http://localhost:8080> | a interface de cálculo |
| <http://localhost:8080/swagger-ui.html> | documentação interativa da API |
| <http://localhost:8080/h2-console> | console do banco (JDBC URL `jdbc:h2:mem:graphdb`, usuário `sa`, sem senha) |

## Grafos de exemplo

O banco é recriado a cada inicialização e semeado com dois grafos, então há
dados para usar imediatamente:

| Id | Arestas (origem, destino, peso) |
|---|---|
| **1** | AB4, AE8 |
| **2** | AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7 |

O grafo é **dirigido**: `AB5` permite ir de A para B, não o contrário. Por isso
não existe caminho de C para A no grafo 2.

## API

### Menor caminho

```bash
curl http://localhost:8080/distance/2/from/A/to/C
# {"distance":9,"path":["A","B","C"]}
```

A→B→C custa 5+4=9, menos que A→D→C (5+8=13) ou A→E→B→C (7+3+4=14).

Responde **404** quando o grafo não existe, quando algum dos vértices não
pertence ao grafo, ou quando não há caminho entre eles.

### Todas as rotas

```bash
curl "http://localhost:8080/routes/2/from/A/to/C?maxStops=4"
# {"routes":[{"route":"ABC","stops":2},{"route":"ADC","stops":2},
#            {"route":"AEBC","stops":3},{"route":"ADEBC","stops":4}]}
```

`maxStops` é opcional e limita o número de paradas. As rotas vêm ordenadas da
mais curta para a mais longa.

### Grafos

```bash
curl http://localhost:8080/graph        # lista todos
curl http://localhost:8080/graph/2      # busca por id

curl -X POST http://localhost:8080/graph \
  -H 'Content-Type: application/json' \
  -d '{"data":[{"source":"A","target":"B","distance":3},
               {"source":"B","target":"C","distance":2}]}'
```

## Testes

```bash
./mvnw test
```

21 testes: o algoritmo de Dijkstra isoladamente (`DijkstraTest`), a camada HTTP
com o servidor no ar (`GraphHttpTest`) e os controllers via MockMvc.

## Empacotar

```bash
./mvnw clean package
java -jar target/dijkstra-graph-app-1.0.0.jar
```

## Stack

Java 21 · Spring Boot 4.1.1 · Spring Data JPA · H2 em memória · Thymeleaf ·
Bootstrap 5 · springdoc-openapi

Os dados vivem em memória: tudo o que você criar desaparece ao parar a
aplicação, e os dois grafos de exemplo voltam na próxima inicialização.
