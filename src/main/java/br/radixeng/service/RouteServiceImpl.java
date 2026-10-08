package br.radixeng.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.radixeng.dijkstra.Dijkstra;
import br.radixeng.dijkstra.Grafo;
import br.radixeng.dijkstra.Vertice;
import br.radixeng.dto.DistanceDTO;
import br.radixeng.dto.RouteDTO;
import br.radixeng.entities.Graph;
import br.radixeng.entities.Route;
import br.radixeng.exception.GraphException;

/**
 * Serviço sem estado: cada chamada constrói a própria adjacência e a própria
 * lista de resultados, para que requisições concorrentes não interfiram entre si.
 */
@Service
public class RouteServiceImpl implements IRouteService {

	private final static Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	private final GraphServiceImpl graphService;

	public RouteServiceImpl(GraphServiceImpl graphService) {
		this.graphService = graphService;
	}

	@Override
	public DistanceDTO findMinimalPath(Long graphId, String town1, String town2) {

		Graph returnedGraph = graphService.findById(graphId);

		if (returnedGraph == null) {
			return null;
		}

		DistanceDTO minimalPath = new DistanceDTO();

		if (town1.equals(town2)) {

			List<String> path = new ArrayList<>();
			path.add(town1);
			path.add(town2);

			minimalPath.setPath(path);
			minimalPath.setDistance(0);

			return minimalPath;
		}

		Grafo graph = new Grafo();

		try {
			graph.setVertices(Dijkstra.lerGrafo(serialize(returnedGraph)));
		} catch (GraphException ge) {
			LOG.warn("Não foi possível interpretar o grafo serializado", ge);
			return null;
		}

		Vertice vTown1 = graph.encontrarVertice(town1);
		Vertice vTown2 = graph.encontrarVertice(town2);

		// Vértice inexistente no grafo: sem este guarda, uma origem desconhecida
		// estourava NullPointerException dentro do algoritmo, e um destino
		// desconhecido devolvia 200 com distance -1.
		if (vTown1 == null || vTown2 == null) {
			return null;
		}

		List<Vertice> resultado = new Dijkstra().encontrarMenorCaminhoDijkstra(graph, vTown1, vTown2);

		// Só a origem no caminho significa que o destino é inalcançável.
		if (resultado.size() == 1) {
			return null;
		}

		List<String> path = new ArrayList<>();

		for (Vertice vPath : resultado) {
			path.add(vPath.getDescricao());
		}

		minimalPath.setPath(path);
		minimalPath.setDistance(resultado.get(resultado.size() - 1).getDistancia());

		return minimalPath;
	}

	@Override
	public List<RouteDTO> findAllRoutes(Long graphId, String town1, String town2, Integer maxStops) {

		Graph returnedGraph = graphService.findById(graphId);

		if (returnedGraph == null) {
			return null;
		}

		Map<String, LinkedHashSet<String>> adjacency = buildAdjacency(returnedGraph);

		List<RouteDTO> found = new ArrayList<>();
		LinkedList<String> visited = new LinkedList<>();
		visited.add(town1);

		depthFirst(adjacency, visited, town2, found);

		return limitByMaxStops(found, maxStops);
	}

	/**
	 * Formato que o Dijkstra.lerGrafo espera: uma linha "origem,destino/peso"
	 * por aresta.
	 */
	private static String serialize(Graph graph) {

		StringBuilder stringfiedGraph = new StringBuilder();

		for (Route route : graph.getData()) {
			stringfiedGraph.append(route.getSource())
					.append(",").append(route.getTarget())
					.append("/").append(route.getDistance())
					.append("\n");
		}

		return stringfiedGraph.toString();
	}

	private static Map<String, LinkedHashSet<String>> buildAdjacency(Graph graph) {

		Map<String, LinkedHashSet<String>> adjacency = new LinkedHashMap<>();

		for (Route route : graph.getData()) {
			adjacency.computeIfAbsent(route.getSource(), key -> new LinkedHashSet<>()).add(route.getTarget());
		}

		return adjacency;
	}

	private static void depthFirst(Map<String, LinkedHashSet<String>> adjacency,
			LinkedList<String> visited, String end, List<RouteDTO> routeStops) {

		LinkedHashSet<String> adjacent = adjacency.get(visited.getLast());

		LinkedList<String> nodes = adjacent != null ? new LinkedList<>(adjacent) : new LinkedList<>();

		for (String node : nodes) {

			if (visited.contains(node)) {
				continue;
			}

			if (node.equals(end)) {
				visited.add(node);
				addPath(visited, routeStops);
				visited.removeLast();
				break;
			}
		}

		for (String node : nodes) {

			if (visited.contains(node) || node.equals(end)) {
				continue;
			}

			visited.addLast(node);
			depthFirst(adjacency, visited, end, routeStops);
			visited.removeLast();
		}
	}

	private static void addPath(LinkedList<String> visited, List<RouteDTO> routeStops) {

		StringBuilder nodes = new StringBuilder();

		for (String node : visited) {
			nodes.append(node);
		}

		routeStops.add(new RouteDTO(nodes.toString(), visited.size() - 1));
	}

	private static List<RouteDTO> limitByMaxStops(List<RouteDTO> routes, Integer maxStops) {

		if (maxStops == null) {
			return routes;
		}

		List<RouteDTO> limited = new ArrayList<>();

		for (RouteDTO route : routes) {
			if (maxStops > 0 && route.getStops() <= maxStops) {
				limited.add(route);
			}
		}

		return limited;
	}
}
