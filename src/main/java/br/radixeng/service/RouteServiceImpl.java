package br.radixeng.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.radixeng.dijkstra.Dijkstra;
import br.radixeng.dijkstra.Grafo;
import br.radixeng.dijkstra.Vertice;
import br.radixeng.dto.DistanceDTO;
import br.radixeng.dto.RouteDTO;
import br.radixeng.entities.Graph;
import br.radixeng.entities.Route;
import br.radixeng.enums.Values;
import br.radixeng.exception.GraphException;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RouteServiceImpl implements IRouteService{

	private final static Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

	@Autowired
	GraphServiceImpl graphService;
	

	private List<RouteDTO> routeStops = new ArrayList<>();
	
	
	private List<RouteDTO> routeBuilded = new ArrayList<>();
	
	@Override
	public DistanceDTO findMinimalPath(Long graphId, String town1, String town2) {
		
		Graph returnedGraph = graphService.findById(graphId);
		
		Grafo graph = new Grafo();
		
		Vertice vTown1 = new Vertice();
		Vertice vTown2 = new Vertice();
		
		Dijkstra algoritmo = new Dijkstra();
		DistanceDTO minimalPath = new DistanceDTO();
		
		List<Vertice> resultado = new ArrayList<Vertice>();
		StringBuilder stringfiedGraph = new StringBuilder();
		
		if(returnedGraph != null && !town1.equals(town2)) {
		
			for(Route route : returnedGraph.getData()) {
				
				stringfiedGraph.append(route.getSource())
								.append(",").append(route.getTarget())
								.append("/").append(route.getDistance())
								.append("\n");
			}
		
			try {
				
				graph.setVertices(Dijkstra.lerGrafo(stringfiedGraph.toString()));
			
			} catch (GraphException ge) {
				
				LOG.warn("Não foi possível interpretar o grafo serializado", ge);
			}
			
			vTown1 = graph.encontrarVertice(town1);
			vTown2 = graph.encontrarVertice(town2);
			
			resultado = algoritmo.encontrarMenorCaminhoDijkstra(graph, vTown1, vTown2);

			List<String> path = new ArrayList<>();
			
			for(Vertice vPath : resultado) {
				path.add(vPath.getDescricao());
			}

			minimalPath.setPath(path);
			
			if(path.size()==1) {
			
				minimalPath.setDistance(-1);
			} else {
				
				minimalPath.setDistance(resultado.get(resultado.size()-1).getDistancia());
			}
			
		} else if(returnedGraph != null && town1.equals(town2)) {
			
			List<String> path = new ArrayList<>();
			path.add(town1);
			path.add(town2);
			
			minimalPath.setPath(path);
			minimalPath.setDistance(0);
			
			return minimalPath;
		
		} else {
			return null;
		}
		
		return minimalPath;
	}
	
	@Override
	public RouteServiceImpl findAllRoutes(Long graphId, String town1, String town2) {
		
		Graph returnedGraph = graphService.findById(graphId);
		
		if(returnedGraph!=null) {
		
			for(Route route : returnedGraph.getData()) {
				this.addEdge(route.getSource(), route.getTarget());
			}
			
			LinkedList<String> visited = new LinkedList();
			visited.add(town1);
	 		
			this.depthFirst(this, visited, town2, routeStops);
			
		} else {
			
			routeStops = new ArrayList<>();
			RouteDTO route = new RouteDTO();
			route.setStops(-1);
			route.setRoute(Values.GRAPH_NOT_FOUND.getCode());
			routeStops.add(route);
		}
		
		return this;
	}
	
	public RouteServiceImpl removeRoutesBy(Integer maxStops){
		
		List<RouteDTO> returnedRoutes = new ArrayList<>();
		
		if(!routeStops.isEmpty()) {
			
			for (RouteDTO route : this.routeStops) {
				
				if(maxStops==null) {
					returnedRoutes.add(route);
					continue;
				}
				
				if(maxStops > 0 && route.getStops() <= maxStops) {
				
					returnedRoutes.add(route);
				}
				
			}
			
			routeBuilded.addAll(returnedRoutes);
			
		} else {
			routeBuilded = new ArrayList<>();
		}
		
		return this;
	}
	
	public List<RouteDTO> buildRoute(){
		
		return routeBuilded;
	}
	
	public void resetRouteStops() {
		this.routeStops.clear();
	}
	
	public void resetRouteBuilded() {
		this.routeBuilded.clear();
	}
	
}
