package br.radixeng.controller;

import static br.radixeng.util.GraphTestUtil.buildedGraph;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.radixeng.Application;

/**
 *
 */
@SpringBootTest(classes = Application.class)
class GraphControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		// webAppContextSetup, e não standaloneSetup: standaloneSetup descarta a
		// configuração real de MVC — conversores de mensagem e handlers de
		// exceção incluídos — e testaria o controller fora da aplicação.
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	void grafoInexistenteDevolve404() throws Exception {
		mockMvc.perform(get("/graph/{id}", 123)).andExpect(status().isNotFound());
	}

	@Test
	void salvaGrafo() throws Exception {

		mockMvc.perform(post("/graph")
						.content(asJsonString(buildedGraph()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
