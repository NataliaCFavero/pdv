package com.zx.pdv.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.zx.pdv.model.Address;
import com.zx.pdv.model.CoverageArea;
import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.service.PdvService;

import rx.Observable;

@RunWith(SpringRunner.class)
@WebMvcTest(PdvController.class)
public class PdvControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PdvService service;
	
	
	private PdvReqResp createPdv() {

		List<Double> point1 = Arrays.asList(30.00, 20.00);
		List<Double> point2 = Arrays.asList(45.00, 40.00);
		List<Double> point3 = Arrays.asList(10.00, 40.00);
		List<Double> point4 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygonDao = Arrays.asList(point1, point2, point3, point4);
		List<List<List<Double>>> areaDao = Arrays.asList(polygonDao);

		List<Double> point11 = Arrays.asList(15.00, 5.00);
		List<Double> point22 = Arrays.asList(40.00, 10.00);
		List<Double> point33 = Arrays.asList(10.00, 20.00);
		List<Double> point44 = Arrays.asList(5.00, 10.00);
		List<Double> point55 = Arrays.asList(15.00, 5.00);
		List<List<Double>> polygon2Dao = Arrays.asList(point11, point22, point33, point44, point55);
		List<List<List<Double>>> area2Dao = Arrays.asList(polygon2Dao);

		List<List<List<List<Double>>>> multipolygon = Arrays.asList(areaDao, area2Dao);

		Address address = new Address(Arrays.asList(-46.57421, -21.785741));
		CoverageArea coverage = new CoverageArea(multipolygon);
		
		PdvReqResp pdv = new PdvReqResp();
		pdv.setId(new UUID(1, 1));
		pdv.setDocument("1432132123891/0001");
		pdv.setOwnerName("Zé da Silva");
		pdv.setTradingName("Adega da Cerveja - Pinheiros");
		pdv.setAddress(address);
		pdv.setCoverageArea(coverage);
		
		return pdv;
	}
	
	
	@Test
	public void testCreatePdvSuccess() throws Exception {
		
		Mockito.when(service.save(Mockito.any(PdvReqResp.class))).thenReturn(Observable.just(createPdv()));
		
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
		mockMvc.perform(post("/pdv")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(payload)
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
	

	@Test
	public void testCreatePdvWithInvalidCnpj() throws Exception {
		
		Mockito.when(service.save(Mockito.any(PdvReqResp.class))).thenReturn(Observable.just(createPdv()));
		
		String payload = "{" 
				+ "\"tradingName\": \"Adega da Cerveja - Pinheiros\"," 
				+ "\"ownerName\": \"Zé da Silva\","
				+ "\"document\": \"28.933.662/0001-12\"," 
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
		mockMvc.perform(post("/pdv")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(payload)
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().is(400));
	}
	
	@Test
	public void testGetPdvByIdSuccess() {
		
	}
	
	@Test
	public void testSearchPdvCloser() {
		
	}

}
