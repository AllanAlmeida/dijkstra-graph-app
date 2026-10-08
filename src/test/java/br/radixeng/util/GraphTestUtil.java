package br.radixeng.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.radixeng.builder.GraphsBuilder;
import br.radixeng.entities.Graph;
import br.radixeng.entities.Route;
import br.radixeng.exception.GraphException;
import br.radixeng.service.GraphServiceImpl;

public class GraphTestUtil {

	private final static Logger LOG = LoggerFactory.getLogger(GraphTestUtil.class);

	private final GraphServiceImpl graphService;

	public GraphTestUtil(GraphServiceImpl graphService) {
		this.graphService = graphService;
	}

	public static Graph buildedGraph() {

		// AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7
		return new GraphsBuilder()
				.route(new Route("A", "B", 5))
				.route(new Route("B", "C", 4))
				.route(new Route("C", "D", 8))
				.route(new Route("D", "C", 8))
				.route(new Route("D", "E", 6))
				.route(new Route("A", "D", 5))
				.route(new Route("C", "E", 2))
				.route(new Route("E", "B", 3))
				.route(new Route("A", "E", 7))
				.builder();
	}

	public void initGraph() {

		try {

			if (graphService.findById(2L) == null) {
				graphService.saveGraph(buildedGraph());
			}

		} catch (GraphException e) {
			LOG.error("Não foi possível salvar o grafo.", e);
		}
	}
}
