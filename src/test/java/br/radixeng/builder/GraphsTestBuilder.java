package br.radixeng.builder;

import java.util.ArrayList;
import java.util.List;

import br.radixeng.entities.Graph;
import br.radixeng.entities.Route;

public class GraphsTestBuilder {

		Graph graph;
		
		List<Route> routes = new ArrayList<Route>();
		
		Long idRoute = 0L;
		
		public GraphsTestBuilder(){
			this.graph = new Graph();
		}
		
		public GraphsTestBuilder route(Route route) {
			routes.add(route);
			graph.setData(routes);
			return this;
		}
		
		public Graph builder() {
			return graph;
		}
	}
