package br.radixeng.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import br.radixeng.builder.GraphsTestBuilder;
import br.radixeng.entities.Graph;
import br.radixeng.entities.Route;
import br.radixeng.exception.GraphException;
import br.radixeng.service.GraphServiceImpl;

@PropertySource({ "classpath:graph-msg.properties" })
public class GraphTestUtil {
	
	private final static Logger LOG = LogManager.getLogger();
	
	@Autowired
    Environment env;
	
	@Autowired
	private GraphServiceImpl graphService;
	
	public GraphTestUtil(GraphServiceImpl graphService) {
		this.graphService = graphService;
	}
	 
	public static Graph buildedGraph() {
		
		//AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7
    	Graph graph = new GraphsTestBuilder()
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
    	
    	return graph;
    }
	
	public void initGraph() {
    	
    	try {
	    	Graph graph = graphService.findById(2L);
	    	
	    	if(graph==null) {
	    		graphService.saveGraph(buildedGraph());
			}

    	} catch(GraphException e) {
    		LOG.error(env.getProperty("msg.graph.erro.save"), e);
    	}
    }

}
