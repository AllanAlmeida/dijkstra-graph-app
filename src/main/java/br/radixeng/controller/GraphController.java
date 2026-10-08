package br.radixeng.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.radixeng.entities.Graph;
import br.radixeng.service.GraphServiceImpl;
import br.radixeng.validation.GraphValidation;

/**
 *
 */
@RestController
public class GraphController {

	private final GraphServiceImpl graphService;

	private final GraphValidation graphValidation = new GraphValidation();

	public GraphController(GraphServiceImpl graphService) {
		this.graphService = graphService;
	}

	@GetMapping("/graph")
	public ResponseEntity<List<Graph>> listAllGraphs() {
		return ResponseEntity.ok(graphService.findAllGraphs());
	}

	@GetMapping("/graph/{id}")
	public ResponseEntity<Graph> getGraphById(@PathVariable long id) {

		Graph graph = this.graphService.findById(id);

		return new ResponseEntity<Graph>(graph, graphValidation.validateReturnGraph(graph));
	}

	@PostMapping("/graph")
	public ResponseEntity<Graph> saveGraph(@RequestBody Graph graph) {

		graphService.saveGraph(graph);

		return new ResponseEntity<Graph>(graph, graphValidation.validateReturnGraph(graph));
	}
}
