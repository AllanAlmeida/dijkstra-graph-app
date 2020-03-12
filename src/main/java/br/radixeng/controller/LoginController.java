package br.radixeng.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.radixeng.jwt.filter.UserCredentials;

/**
* Usado para obter o token do JWT
*/
@RestController
public class LoginController {

	@RequestMapping(value = "/logingraph", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> jwtToken(UserCredentials user) {
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
