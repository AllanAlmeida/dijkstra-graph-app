package br.radixeng.service;

import br.radixeng.entities.Graph;
import br.radixeng.exception.GraphException;

public interface IGraphService {

	Graph findById(Long id);
	
	Iterable<Graph> findAllGraphs();
	
	void saveGraph(Graph graph) throws GraphException;
}
