package br.radixeng.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.radixeng.dto.DistanceDTO;
import br.radixeng.dto.RouteDTO;
import br.radixeng.enums.Values;
import br.radixeng.service.RouteServiceImpl;

/**
*
*/
@RestController
public class RouteController {
	
	@Autowired
	private RouteServiceImpl routeService;
	
	public RouteController(RouteServiceImpl routeService) {
	       this.routeService = routeService;
	}
	
	@RequestMapping(value = "/routes/{graphId}/from/{town1}/to/{town2}", method = RequestMethod.GET)
    public ResponseEntity<RouteDTO> listAllRoutes(
    		@PathVariable("graphId") Long graphId,
    		@PathVariable("town1") String town1,
    		@PathVariable("town2") String town2,
    		@RequestParam(value="maxStops", required=false) Integer maxStops
    		) {
		
		List<RouteDTO> listRoutes = new ArrayList<>();
		
		resetFields();
		
		listRoutes.addAll(routeService.findAllRoutes(graphId, town1, town2).removeRoutesBy(maxStops).buildRoute());
		
		RouteDTO routes = checkEmptyListAndTowns(town1, town2, listRoutes);
		
		return routes!=null 
				? new ResponseEntity<RouteDTO>(routes, HttpStatus.OK)
				: new ResponseEntity<RouteDTO>(routes, HttpStatus.NOT_FOUND);
    }

	@RequestMapping(value = "/distance/{graphId}/from/{town1}/to/{town2}", method = RequestMethod.GET)
    public ResponseEntity<DistanceDTO> listMinimumPath(
    		@PathVariable("graphId") Long graphId,
    		@PathVariable("town1") String town1,
    		@PathVariable("town2") String town2
    		) {
		
		DistanceDTO result = routeService.findMinimalPath(graphId, town1, town2);
		
		return result!=null 
				? new ResponseEntity<DistanceDTO>(result, HttpStatus.OK)
				: new ResponseEntity<DistanceDTO>(result, HttpStatus.NOT_FOUND);
    }
	
	private RouteDTO checkEmptyListAndTowns(String town1, String town2, List<RouteDTO> listRoutes) {
		
		RouteDTO routes = new RouteDTO();
		
		if(listRoutes.isEmpty() && town1.equals(town2)) {
			
			routes.setRoute(town1+town2);
			routes.setStops(0);
			listRoutes.add(routes);
			
		} else if(listRoutes.isEmpty() && !town1.equals(town2)) {
			
			listRoutes.add(routes);
			
		} else if(Values.GRAPH_NOT_FOUND.getCode().equals(listRoutes.get(0).getRoute())) {
			
			return null;
		
		} else {
			
			Collections.sort(listRoutes, (rout1, rout2) -> rout1.getStops() - rout2.getStops());
			routes.setRoutes(listRoutes);
		}
		
		return routes;
	}
	
	private void resetFields() {
		routeService.resetMap();
		routeService.resetRouteStops();
		routeService.resetRouteBuilded();
	}
}
