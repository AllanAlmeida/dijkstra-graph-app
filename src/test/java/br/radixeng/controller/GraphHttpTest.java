package br.radixeng.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import br.radixeng.Application;

/**
 * Exercita a aplicação por HTTP de verdade, com o Tomcat no ar.
 *
 * Substitui o antigo teste Selenium, que dependia de um chromedriver.exe
 * versionado no repositório, de um servidor já rodando em :8080 e de
 * Thread.sleep entre as interações. A cobertura é a mesma, sem navegador.
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraphHttpTest {

	@LocalServerPort
	private int port;

	private final HttpClient client = HttpClient.newHttpClient();

	private HttpResponse<String> get(String path) throws Exception {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + path))
				.header("Accept", "application/json")
				.GET()
				.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private HttpResponse<String> post(String path, String body) throws Exception {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + path))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void menorCaminhoNoGrafoDeExemplo() throws Exception {

		HttpResponse<String> response = get("/distance/2/from/A/to/C");

		assertEquals(200, response.statusCode());
		assertEquals("{\"distance\":9,\"path\":[\"A\",\"B\",\"C\"]}", response.body());
	}

	@Test
	void todasAsRotasComLimiteDeParadas() throws Exception {

		HttpResponse<String> response = get("/routes/2/from/A/to/C?maxStops=4");

		assertEquals(200, response.statusCode());
		assertEquals("{\"routes\":["
				+ "{\"route\":\"ABC\",\"stops\":2},"
				+ "{\"route\":\"ADC\",\"stops\":2},"
				+ "{\"route\":\"AEBC\",\"stops\":3},"
				+ "{\"route\":\"ADEBC\",\"stops\":4}"
				+ "]}", response.body());
	}

	@Test
	void grafoComVerticeQueSoApareceComoDestino() throws Exception {
		// Grafo 1: AB4 e AE8. B e E nunca são origem, e antes da correção em
		// Dijkstra.lerGrafo nenhuma consulta conseguia chegar neles.
		HttpResponse<String> response = get("/distance/1/from/A/to/B");

		assertEquals(200, response.statusCode());
		assertEquals("{\"distance\":4,\"path\":[\"A\",\"B\"]}", response.body());
	}

	@Test
	void origemInexistenteDevolve404() throws Exception {
		// Antes respondia 500 com NullPointerException e o stack trace no corpo
		assertEquals(404, get("/distance/2/from/Z/to/A").statusCode());
	}

	@Test
	void destinoInexistenteDevolve404() throws Exception {
		// Antes respondia 200 com {"distance":-1,"path":["A"]}
		assertEquals(404, get("/distance/2/from/A/to/Z").statusCode());
	}

	@Test
	void grafoInexistenteDevolve404() throws Exception {
		assertEquals(404, get("/distance/99/from/A/to/C").statusCode());
	}

	@Test
	void origemIgualAoDestinoTemDistanciaZero() throws Exception {

		HttpResponse<String> response = get("/distance/2/from/A/to/A");

		assertEquals(200, response.statusCode());
		assertEquals("{\"distance\":0,\"path\":[\"A\",\"A\"]}", response.body());
	}

	@Test
	void paginaEOsAssetsSaoServidos() throws Exception {
		// O caminho do webjar vai sem versão: é o que impede o 404 de CSS que a
		// página tinha quando o HTML fixava 3.3.7 e o pom entregava 3.4.1.
		assertEquals(200, get("/distanciaMinima").statusCode());
		assertEquals(200, get("/js/distancia-minima.js").statusCode());

		HttpResponse<String> css = get("/webjars/bootstrap/css/bootstrap.min.css");

		assertEquals(200, css.statusCode());
		assertTrue(css.body().contains("Bootstrap"), "o CSS do Bootstrap deve ser servido");
	}

	@Test
	void listaOsGrafosSemeados() throws Exception {
		// Assertivas por conteúdo, e não por id ou por contagem: o contexto do
		// Spring é compartilhado entre classes de teste, e um POST em outra
		// classe acrescenta grafos.
		HttpResponse<String> response = get("/graph");

		assertEquals(200, response.statusCode());
		assertTrue(response.body().startsWith("["), "deve devolver uma lista");
		assertTrue(response.body().contains("\"source\":\"A\",\"target\":\"B\",\"distance\":4"),
				"aresta AB4 do grafo 1");
		assertTrue(response.body().contains("\"source\":\"C\",\"target\":\"E\",\"distance\":2"),
				"aresta CE2 do grafo 2");
	}

	@Test
	void rotasDeGrafoInexistenteDevolve404() throws Exception {
		assertEquals(404, get("/routes/99/from/A/to/C").statusCode());
	}

	@Test
	void rotasSemMaxStopsDevolveTodas() throws Exception {

		HttpResponse<String> response = get("/routes/2/from/A/to/C");

		assertEquals(200, response.statusCode());
		assertEquals("{\"routes\":["
				+ "{\"route\":\"ABC\",\"stops\":2},"
				+ "{\"route\":\"ADC\",\"stops\":2},"
				+ "{\"route\":\"AEBC\",\"stops\":3},"
				+ "{\"route\":\"ADEBC\",\"stops\":4}"
				+ "]}", response.body());
	}

	@Test
	void maxStopsZeroOuNegativoNaoDevolveRota() throws Exception {
		// RouteDTO é serializado com NON_EMPTY, então a resposta vazia vira {}
		assertEquals("{}", get("/routes/2/from/A/to/C?maxStops=0").body());
		assertEquals("{}", get("/routes/2/from/A/to/C?maxStops=-1").body());
	}

	@Test
	void destinoInalcancavelComVerticesValidosDevolve404() throws Exception {
		// C e A existem no grafo 2, mas nenhuma aresta chega em A.
		// Exercita o caminho resultado.size() == 1 em RouteServiceImpl.
		assertEquals(404, get("/distance/2/from/C/to/A").statusCode());
	}

	@Test
	void payloadMalformadoDevolve400() throws Exception {
		assertEquals(400, post("/graph", "{\"data\":\"nao-e-uma-lista\"}").statusCode());
	}
}
