package br.radixeng.controller;

import static br.radixeng.util.GraphTestUtil.buildedGraph;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.radixeng.Application;

/**
 *
 */
@RunWith(SpringRunner.class)	
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class GraphControllerTest {
    
	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
	private GraphController graphController;
    
    @Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(graphController).build();
    }
        
    @Test
	public void testGraphNotFound() throws Exception {
        String inesistentGraphId = "123";
		mockMvc.perform(get(String.format("/graph/%s",inesistentGraphId))).andExpect(status().isNotFound());
	}
    
    @Test
   	public void testSaveGraph() throws Exception {
    	
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
