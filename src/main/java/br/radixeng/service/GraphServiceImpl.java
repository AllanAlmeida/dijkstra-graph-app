package br.radixeng.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.radixeng.entities.Graph;
import br.radixeng.exception.GraphException;
import br.radixeng.repository.GraphRepository;

@Service
public class GraphServiceImpl implements IGraphService{
	 
	@Autowired
	private GraphRepository graphRepository;
	
	public GraphServiceImpl() {
		//default constructor
	}
	
	@Override
	public Graph findById(Long id) {
		
		Optional<Graph> graphOptional = graphRepository.findById(id);
		
		if (graphOptional.isPresent()) {
			
			return graphOptional.get();
		} 
		
		return null;
	}
	
	@Override
	public List<Graph> findAllGraphs() {
		return (List<Graph>) graphRepository.findAll();
	}
	
	@Override
	public void saveGraph(Graph graph) throws GraphException {
		graphRepository.save(graph);
	}
}
