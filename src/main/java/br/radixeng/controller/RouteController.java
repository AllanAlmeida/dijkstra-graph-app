package br.radixeng.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.radixeng.dto.DistanceDTO;
import br.radixeng.dto.RouteDTO;
import br.radixeng.service.IRouteService;

/**
 *
 */
@RestController
public class RouteController {

	private final IRouteService routeService;

	public RouteController(IRouteService routeService) {
		this.routeService = routeService;
	}

	@GetMapping("/routes/{graphId}/from/{town1}/to/{town2}")
	public ResponseEntity<RouteDTO> listAllRoutes(
			@PathVariable("graphId") Long graphId,
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2,
			@RequestParam(value = "maxStops", required = false) Integer maxStops) {

		List<RouteDTO> listRoutes = routeService.findAllRoutes(graphId, town1, town2, maxStops);

		if (listRoutes == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(asResponse(town1, town2, listRoutes));
	}

	@GetMapping("/distance/{graphId}/from/{town1}/to/{town2}")
	public ResponseEntity<DistanceDTO> listMinimumPath(
			@PathVariable("graphId") Long graphId,
			@PathVariable("town1") String town1,
			@PathVariable("town2") String town2) {

		DistanceDTO result = routeService.findMinimalPath(graphId, town1, town2);

		return result != null
				? ResponseEntity.ok(result)
				: ResponseEntity.notFound().build();
	}

	/**
	 * Origem igual ao destino é uma rota de zero paradas; fora disso, as rotas
	 * encontradas vêm ordenadas da mais curta para a mais longa.
	 */
	private static RouteDTO asResponse(String town1, String town2, List<RouteDTO> listRoutes) {

		RouteDTO routes = new RouteDTO();

		if (listRoutes.isEmpty()) {

			if (town1.equals(town2)) {
				routes.setRoute(town1 + town2);
				routes.setStops(0);
			}

			return routes;
		}

		listRoutes.sort(Comparator.comparingInt(RouteDTO::getStops));
		routes.setRoutes(listRoutes);

		return routes;
	}
}
