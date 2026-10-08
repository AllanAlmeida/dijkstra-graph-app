package br.radixeng.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.radixeng.Application;
import br.radixeng.service.GraphServiceImpl;
import br.radixeng.util.GraphTestUtil;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
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
	void testRoutCC() throws Exception {

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

	@Test
	void testRoutACMaxStops4() throws Exception {

		String graphId = "2";
		String town1 = "A";
		String town2 = "C";
		String maxStops = "4";

		MvcResult mvcResult = mockMvcBuild.perform(get(String.format("/routes/%s/from/%s/to/%s", graphId, town1, town2)).param("maxStops", maxStops))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();

		String response = mvcResult.getResponse().getContentAsString();
		assertEquals(expectedGraph(), response);
	}

	private static String expectedGraph() {
		return "{\"routes\":["
				+ "{\"route\":\"ABC\",\"stops\":2},"
				+ "{\"route\":\"ADC\",\"stops\":2},"
				+ "{\"route\":\"AEBC\",\"stops\":3},"
				+ "{\"route\":\"ADEBC\",\"stops\":4}"
				+ "]}";
	}
}
