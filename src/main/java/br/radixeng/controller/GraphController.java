package br.radixeng.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.radixeng.entities.Graph;
import br.radixeng.exception.GraphException;
import br.radixeng.service.GraphServiceImpl;
import br.radixeng.validation.GraphValidation;

/**
 *
 */
@RestController
@PropertySource({ "classpath:graph-msg.properties" })
public class GraphController {
    
	private final static Logger LOG = LogManager.getLogger();
	
	@Autowired
    Environment env;
	
	@Autowired
	private GraphServiceImpl graphService;
	
	private GraphValidation graphValidation;
	
	public GraphController(GraphServiceImpl graphService) {
	       
		this.graphService = graphService;
		this.graphValidation = new GraphValidation();
	}
		
	@RequestMapping(value = "/graph", method = RequestMethod.GET)
    public ResponseEntity<List<Graph>> listAllGraphs() {
        
		List<Graph> graphs = graphService.findAllGraphs();
        return new ResponseEntity<List<Graph>>(graphs, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/graph/{id}", method = RequestMethod.GET)
    public ResponseEntity<Graph> getGraphById(@PathVariable long id){
		
		Graph graph = this.graphService.findById(id);
		return new ResponseEntity<Graph>(graph, graphValidation.validateReturnGraph(graph));
    }
	
	@RequestMapping(value = "/graph", method =  RequestMethod.POST)
    public ResponseEntity<Graph> saveGraph(@RequestBody Graph graph){
       
		try {
			
			graphService.saveGraph(graph);
			
		} catch (GraphException e) {
			graph = null;
			LOG.error(env.getProperty("msg.graph.erro.save"), e);
		}
        return new ResponseEntity<Graph>(graph, graphValidation.validateReturnGraph(graph));
    }
}
