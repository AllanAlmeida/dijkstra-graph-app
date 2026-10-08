package br.radixeng.service;

import br.radixeng.entities.Graph;

public interface IGraphService {

	Graph findById(Long id);
	
	Iterable<Graph> findAllGraphs();
	
	void saveGraph(Graph graph);
}
