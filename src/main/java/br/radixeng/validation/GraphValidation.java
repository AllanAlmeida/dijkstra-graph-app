package br.radixeng.validation;

import org.springframework.http.HttpStatus;

import br.radixeng.entities.Graph;

public class GraphValidation {

	public HttpStatus validateReturnGraph(Graph graph) {
		if(graph != null) {
			return HttpStatus.OK;
		} 
		return HttpStatus.NOT_FOUND;
	}
}
