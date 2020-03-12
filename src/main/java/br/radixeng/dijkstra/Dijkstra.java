package br.radixeng.dijkstra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	
	public static List<Vertice> lerGrafo(String graphSerialize) throws GraphException {

		Grafo g = new Grafo();
		
		Vertice v;
		
		String vertices[];
		String linha;
		
		ArrayList<String[]> s1 = new ArrayList<String[]>();

		try {
			
			Reader inputString = new StringReader(graphSerialize);
	        
			BufferedReader br = new BufferedReader(inputString);

			Map<String, Vertice> mapa = new HashMap<String, Vertice>();

			while ((linha = br.readLine()) != null) {

				if (linha.contains(",")) {
					
					s1.add(linha.split("/"));
					vertices = s1.get(0)[0].split(",");

					v = (Vertice) mapa.get(vertices[0]);
					
					if (v == null) {
						v = new Vertice();
					}
					
					List<Vertice> vizinhosAtual = new ArrayList<Vertice>();
					List<Aresta> arestasAtual = new ArrayList<Aresta>();
					
					v.setDescricao(vertices[0]);
					
					mapa.put(vertices[0], v);

					if (linha.contains("/")) {

						String pesoArestas[] = s1.get(0)[1].split(",");

						for (int i = 1; i < vertices.length; i++) {
							
							Vertice vit;
							vit = mapa.get(vertices[i]);
							
							if (vit == null) {
								vit = new Vertice();
							}
							
							vit.setDescricao(vertices[i]);
							vizinhosAtual.add(vit);
							mapa.put(vertices[i], vit);

							Aresta ait = new Aresta(v, vit);
							ait.setPeso(Integer.parseInt(pesoArestas[i - 1]));
							arestasAtual.add(ait);
						}
						
						v.setVizinhos(vizinhosAtual);
						v.setArestas(arestasAtual);
					}
				
				} else {

					v = (Vertice) mapa.get(linha);
				
					if (v == null) {
						v = new Vertice();
					}
					
					v.setDescricao(linha);
					mapa.put(linha, v);
				}

				g.adicionarVertice(v);
				s1.clear();
			}

			// catch do BufferedReader
		} catch (FileNotFoundException e) {
			
			throw new GraphException("Referência de arquivo não encontrada", e);
		
		} catch (IOException e) {
			
			throw new GraphException("Erro ao registrar a referência do aquivo", e);
		}
		
		return g.getVertices();
	}
}
