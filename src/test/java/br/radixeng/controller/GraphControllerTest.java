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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.radixeng.Application;

/**
 *
 */
@SpringBootTest(classes = Application.class)
class GraphControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private GraphController graphController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(graphController).build();
	}

	@Test
	void testGraphNotFound() throws Exception {
		String inesistentGraphId = "123";
		mockMvc.perform(get(String.format("/graph/%s", inesistentGraphId))).andExpect(status().isNotFound());
	}

	@Test
	void testSaveGraph() throws Exception {

		this.mockMvc.perform(post("/graph").content(asJsonString(buildedGraph()))
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
