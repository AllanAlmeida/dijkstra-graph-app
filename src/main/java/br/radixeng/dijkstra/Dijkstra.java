package br.radixeng.dijkstra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.radixeng.exception.GraphException;



public class Dijkstra {

	List<Vertice> menorCaminho = new ArrayList<Vertice>();
	Vertice verticeCaminho = new Vertice();
	Vertice atual = new Vertice();
	Vertice vizinho = new Vertice();
	List<Vertice> naoVisitados = new ArrayList<Vertice>();
	
	public List<Vertice> encontrarMenorCaminhoDijkstra(Grafo grafo, Vertice v1, Vertice v2) {

		menorCaminho.add(v1);

		for (int i = 0; i < grafo.getVertices().size(); i++) {

			if (grafo.getVertices().get(i).getDescricao().equals(v1.getDescricao())) {

				grafo.getVertices().get(i).setDistancia(0);

			} else {

				grafo.getVertices().get(i).setDistancia(9999);
			}
			this.naoVisitados.add(grafo.getVertices().get(i));
		}

		Collections.sort(naoVisitados);

		while (!this.naoVisitados.isEmpty()) {

			atual = this.naoVisitados.get(0);

			/*
			 * Para cada vizinho (cada aresta), calcula-se a sua possivel
			 * distancia, somando a distancia do vertice atual com a da aresta
			 * correspondente. Se essa distancia for menor que a distancia do
			 * vizinho, esta eh atualizada.
			 */
			for (int i = 0; i < atual.getArestas().size(); i++) {

				vizinho = atual.getArestas().get(i).getDestino();
			
				if (!vizinho.verificarVisita()) {

					// Comparando a distância do vizinho com a possível
					// distância
					if (vizinho.getDistancia() > (atual.getDistancia() + atual.getArestas().get(i).getPeso())) {

						vizinho.setDistancia(atual.getDistancia() + atual.getArestas().get(i).getPeso());
						vizinho.setPai(atual);

						/*
						 * Se o vizinho eh o vertice procurado, e foi feita uma
						 * mudanca na distancia, a lista com o menor caminho
						 * anterior eh apagada, pois existe um caminho menor
						 * vertices pais, ateh o vertice origem.
						 */
						if (vizinho == v2) {
							
							menorCaminho.clear();
							verticeCaminho = vizinho;
							menorCaminho.add(vizinho);
							
							while (verticeCaminho.getPai() != null) {

								menorCaminho.add(verticeCaminho.getPai());
								verticeCaminho = verticeCaminho.getPai();

							}
							
							// Ordena a lista do menor caminho, para que ele
							// seja exibido da origem ao destino.
							Collections.sort(menorCaminho);
						}
					}
				}
			}
			
			// Marca o vertice atual como visitado e o retira da lista de nao visitados
			atual.visitar();
			this.naoVisitados.remove(atual);
			
			/*
			 * Ordena a lista, para que o vertice com menor distancia fique na
			 * primeira posicao
			 */
			Collections.sort(naoVisitados);
		}

		return menorCaminho;
	}
	
	/**
	 * Interpreta o grafo serializado, uma aresta por linha no formato
	 * "origem,destino/peso".
	 *
	 * Devolve cada vértice uma única vez, inclusive os que aparecem apenas como
	 * destino: registrar só as origens deixava vértices-sumidouro invisíveis
	 * para Grafo.encontrarVertice, e consultas para eles não achavam caminho.
	 */
	public static List<Vertice> lerGrafo(String graphSerialize) throws GraphException {

		Map<String, Vertice> mapa = new LinkedHashMap<String, Vertice>();

		try (BufferedReader br = new BufferedReader(new StringReader(graphSerialize))) {

			String linha;

			while ((linha = br.readLine()) != null) {

				linha = linha.trim();

				if (linha.isEmpty()) {
					continue;
				}

				String[] partes = linha.split("/");
				String[] vertices = partes[0].split(",");

				Vertice origem = vertice(mapa, vertices[0]);

				if (partes.length < 2) {
					continue;
				}

				String[] pesoArestas = partes[1].split(",");

				List<Vertice> vizinhosAtual = new ArrayList<Vertice>();
				List<Aresta> arestasAtual = new ArrayList<Aresta>();

				for (int i = 1; i < vertices.length; i++) {

					Vertice destino = vertice(mapa, vertices[i]);
					vizinhosAtual.add(destino);

					Aresta aresta = new Aresta(origem, destino);
					aresta.setPeso(Integer.parseInt(pesoArestas[i - 1]));
					arestasAtual.add(aresta);
				}

				origem.setVizinhos(vizinhosAtual);
				origem.setArestas(arestasAtual);
			}

		} catch (IOException e) {

			throw new GraphException("Erro ao ler o grafo serializado", e);

		} catch (NumberFormatException e) {

			throw new GraphException("Peso de aresta inválido no grafo serializado", e);
		}

		return new ArrayList<Vertice>(mapa.values());
	}

	private static Vertice vertice(Map<String, Vertice> mapa, String descricao) {

		return mapa.computeIfAbsent(descricao, nome -> {

			Vertice vertice = new Vertice();
			vertice.setDescricao(nome);

			return vertice;
		});
	}
}
