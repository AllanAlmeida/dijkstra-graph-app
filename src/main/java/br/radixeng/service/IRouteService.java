package br.radixeng.service;

import java.util.List;

import br.radixeng.dto.DistanceDTO;
import br.radixeng.dto.RouteDTO;

public interface IRouteService {

	/**
	 * Menor caminho entre dois vértices, ou {@code null} se o grafo não existir,
	 * se algum dos vértices não pertencer a ele, ou se não houver caminho.
	 */
	DistanceDTO findMinimalPath(Long graphId, String town1, String town2);

	/**
	 * Todas as rotas possíveis entre dois vértices, opcionalmente limitadas a
	 * {@code maxStops} paradas. Devolve {@code null} se o grafo não existir, e
	 * uma lista vazia se não houver rota.
	 */
	List<RouteDTO> findAllRoutes(Long graphId, String town1, String town2, Integer maxStops);
}
