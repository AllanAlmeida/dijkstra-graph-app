package br.radixeng.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.radixeng.Application;
import br.radixeng.service.GraphServiceImpl;
import br.radixeng.util.GraphTestUtil;

@SpringBootTest(classes = Application.class)
class RouteControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private GraphServiceImpl graphService;

	private MockMvc mockMvcBuild;

	@BeforeEach
	void setUp() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		mockMvcBuild = builder.build();
		new GraphTestUtil(graphService).initGraph();
	}

	@Test
	void rotaDeUmVerticeParaEleMesmoTemZeroParadas() throws Exception {

		String graphId = "2";
		String town1 = "C";
		String town2 = "C";
		String maxStops = "3";

		ResultMatcher expected = MockMvcResultMatchers.jsonPath("stops").value(0);
		mockMvcBuild.perform(get(String.format("/routes/%s/from/%s/to/%s", graphId, town1, town2)).param("maxStops", maxStops))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(expected);
	}

}
