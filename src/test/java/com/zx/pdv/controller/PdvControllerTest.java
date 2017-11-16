package com.zx.pdv.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.service.PdvService;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import rx.Observable;

@RunWith(SpringRunner.class)
@WebMvcTest(PdvController.class)
public class PdvControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PdvService service;
	
	@BeforeClass
	public static void setUp() {
	    FixtureFactoryLoader.loadTemplates("com.zx.pdv.template");
	}

	@Test
	public void testCreatePdvSuccess() throws Exception {
		PdvReqResp pdv = Fixture.from(PdvReqResp.class).gimme("complete");
		
		Mockito.when(service.save(Mockito.any(PdvReqResp.class))).thenReturn(Observable.just(pdv));
		
		String payload = "{" 
				+ "\"tradingName\": \"Adega da Cerveja - Pinheiros\"," 
				+ "\"ownerName\": \"Zé da Silva\","
				+ "\"document\": \"10.164.873/0001-93\"," 
				+ "\"coverageArea\": {" 
					+ "\"type\": \"MultiPolygon\","
					+ "\"coordinates\": [" 
						+ "[[[30, 20], [45, 40], [10, 40], [30, 20]]],"
						+ "[[[15, 5], [40, 10], [10, 20], [5, 10], [15, 5]]]" 
						+ "]" 
					+ "}," 
				+ "\"address\": {"
					+ "\"type\": \"Point\"," + "\"coordinates\": [-46.57421, -21.785741]}" 
				+ "}";
		mockMvc.perform(post("/pdv/")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(payload))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.id", is(pdv.getId().toString())));
	}
			
	@Test
	public void testGetPdvByIdSuccess() throws Exception {
		PdvReqResp pdv = Fixture.from(PdvReqResp.class).gimme("complete");
		when(service.findById(pdv.getId())).thenReturn(Observable.just(pdv));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/pdv/{id}", pdv.getId())
	            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
	            .andExpect(status().is(200))
	            .andExpect(jsonPath("$.id", is(pdv.getId().toString())))
	            .andExpect(jsonPath("$.document", is(pdv.getDocument())));
	            	
		verify(service, times(1)).findById(pdv.getId());
	}
	
	@Test
	public void testNoFoundPdvById() throws Exception {
		UUID id = new UUID(1, 1);
		when(service.findById(id)).thenReturn(Observable.error(new IllegalArgumentException("Pdv not found")));
		
		mockMvc.perform(get("/pdv/{id}", id.toString())
	            .contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(status().is(400));
		
		verify(service, times(1)).findById(id);      
	}
	
	@Test
	public void testFindCloserPdv() throws Exception {
		PdvReqResp pdv = Fixture.from(PdvReqResp.class).gimme("complete");
		Double lat = pdv.getAddress().getCoordinates().get(0);
		Double lng = pdv.getAddress().getCoordinates().get(1);
		when(service.findCloserPdv(lat, lng)).thenReturn(Observable.just(pdv));
		
		mockMvc.perform(get("/pdv/{lat}/{lng}", lat, lng)
	            .contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(status().is(200))
	            .andExpect(jsonPath("$.id", is(pdv.getId().toString())))
	            .andExpect(jsonPath("$.document", is(pdv.getDocument())));
		
		verify(service, times(1)).findCloserPdv(lat, lng);		
	}
	
	@Test
	public void testNotFindPdvCloser() throws Exception {
		when(service.findCloserPdv(10.0, 30.0)).thenReturn(Observable.error(new IllegalArgumentException("Pdv not found")));
		
		mockMvc.perform(get("/pdv/{lat}/{lng}", 10, 30)
	            .contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(status().is(400));
		
		verify(service, times(1)).findCloserPdv(10.0, 30.0);
	}

}
