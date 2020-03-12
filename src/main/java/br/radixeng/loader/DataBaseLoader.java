package br.radixeng.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.radixeng.builder.GraphsBuilder;
import br.radixeng.entities.Graph;
import br.radixeng.entities.Route;
import br.radixeng.repository.GraphRepository;

@Component
public class DataBaseLoader implements CommandLineRunner {

	private final GraphRepository graph;
	
	@Autowired
	public DataBaseLoader(GraphRepository graphRepository) {
		this.graph = graphRepository;
	}

	@Override
	public void run(String... strings) throws Exception {
		
		Graph graph1 = new Graph();
		graph1.getData().add(new Route("A", "B", 4));
		graph1.getData().add(new Route("A", "E", 8));
		this.graph.saveAndFlush(graph1);
		
		Graph graph2 = new GraphsBuilder()
				.route(new Route("A","B", 5))
				.route(new Route("B","C", 4))
				.route(new Route("C","D", 8))
				.route(new Route("D","C", 8))
				.route(new Route("D","E", 6))
				.route(new Route("A","D", 5))
				.route(new Route("C","E", 2))
				.route(new Route("E","B", 3))
				.route(new Route("A","E", 7))
			.builder();
		this.graph.saveAndFlush(graph2);
	}
}
