package br.radixeng.dijkstra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.radixeng.exception.GraphException;

/**
 * Testes do algoritmo em si, sem Spring e sem banco.
 */
class DijkstraTest {

	/** Mesmo grafo de exemplo do DataBaseLoader: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7. */
	private static final String GRAFO = """
			A,B/5
			B,C/4
			C,D/8
			D,C/8
			D,E/6
			A,D/5
			C,E/2
			E,B/3
			A,E/7
			""";

	private static Grafo grafo() throws GraphException {

		Grafo grafo = new Grafo();
		grafo.setVertices(Dijkstra.lerGrafo(GRAFO));

		return grafo;
	}

	private static List<String> descricoes(List<Vertice> caminho) {

		List<String> nomes = new ArrayList<>();

		for (Vertice vertice : caminho) {
			nomes.add(vertice.getDescricao());
		}

		return nomes;
	}

	private static void assertMenorCaminho(String origem, String destino, int distancia, List<String> esperado)
			throws GraphException {

		Grafo grafo = grafo();

		List<Vertice> caminho = new Dijkstra().encontrarMenorCaminhoDijkstra(
				grafo, grafo.encontrarVertice(origem), grafo.encontrarVertice(destino));

		assertEquals(esperado, descricoes(caminho));
		assertEquals(distancia, caminho.get(caminho.size() - 1).getDistancia());
	}

	@Test
	void menorCaminhoPreferindoDuasArestasAUmaDireta() throws Exception {
		// A->B->C (5+4=9) é menor que A->D->C (5+8=13) e que A->E->B->C (7+3+4=14)
		assertMenorCaminho("A", "C", 9, List.of("A", "B", "C"));
	}

	@Test
	void menorCaminhoPreferindoArestaDireta() throws Exception {
		// A->E direto (7) é menor que A->D->E (5+6=11) e A->B->C->E (5+4+2=11)
		assertMenorCaminho("A", "E", 7, List.of("A", "E"));
	}

	@Test
	void menorCaminhoComUmaUnicaAresta() throws Exception {
		assertMenorCaminho("B", "C", 4, List.of("B", "C"));
	}

	@Test
	void menorCaminhoQueNaoPassaPelaArestaMaisCurta() throws Exception {
		// D->E->B (6+3=9), e não D->C->E->B (8+2+3=13)
		assertMenorCaminho("D", "B", 9, List.of("D", "E", "B"));
	}

	@Test
	void destinoInalcancavelDevolveSoAOrigem() throws Exception {
		// Nenhuma aresta chega em A, então C->A não tem caminho
		Grafo grafo = grafo();

		List<Vertice> caminho = new Dijkstra().encontrarMenorCaminhoDijkstra(
				grafo, grafo.encontrarVertice("C"), grafo.encontrarVertice("A"));

		assertEquals(List.of("C"), descricoes(caminho));
	}

	@Test
	void verticeInexistenteNaoEEncontrado() throws Exception {
		// É a pré-condição do guarda em RouteServiceImpl: sem ele, uma origem
		// desconhecida estourava NullPointerException dentro do algoritmo.
		assertNull(grafo().encontrarVertice("Z"));
	}

	@Test
	void lerGrafoInterpretaTodosOsVertices() throws Exception {
		assertEquals(List.of("A", "B", "C", "D", "E"),
				descricoes(grafo().getVertices()).stream().sorted().toList());
	}

	@Test
	void cadaVerticeApareceUmaUnicaVez() throws Exception {
		// lerGrafo chamava adicionarVertice uma vez por linha, então a origem de
		// várias arestas vinha repetida na lista (A aparecia 3 vezes neste grafo).
		assertEquals(5, grafo().getVertices().size());
	}

	@Test
	void verticeQueSoApareceComoDestinoEEncontrado() throws Exception {
		// Mesmo grafo 1 do DataBaseLoader: B e E nunca são origem de aresta.
		// Antes só as origens eram registradas como vértices, então
		// encontrarVertice("B") devolvia null e não havia caminho A->B.
		Grafo grafo = new Grafo();
		grafo.setVertices(Dijkstra.lerGrafo("A,B/4\nA,E/8\n"));

		assertNotNull(grafo.encontrarVertice("B"));
		assertNotNull(grafo.encontrarVertice("E"));

		List<Vertice> caminho = new Dijkstra().encontrarMenorCaminhoDijkstra(
				grafo, grafo.encontrarVertice("A"), grafo.encontrarVertice("B"));

		assertEquals(List.of("A", "B"), descricoes(caminho));
		assertEquals(4, caminho.get(caminho.size() - 1).getDistancia());
	}
}
