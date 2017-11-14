package com.zx.pdv.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PdvTest {

	@Test
	public void serialize() throws JsonProcessingException, JSONException {
		ObjectMapper mapper = new ObjectMapper();

		Address address = new Address(Arrays.asList(-40.0, 30.0));
		List<Double> point1 = Arrays.asList(30.00, 20.00);
		List<Double> point2 = Arrays.asList(45.00, 40.00);
		List<Double> point3 = Arrays.asList(10.00, 40.00);
		List<Double> point4 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygon = Arrays.asList(point1, point2, point3, point4);
		List<List<List<Double>>> area = Arrays.asList(polygon);

		List<Double> point11 = Arrays.asList(30.00, 20.00);
		List<Double> point22 = Arrays.asList(45.00, 40.00);
		List<Double> point33 = Arrays.asList(10.00, 40.00);
		List<Double> point44 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygon2 = Arrays.asList(point11, point22, point33, point44);
		List<List<List<Double>>> area2 = Arrays.asList(polygon2);

		List<List<List<List<Double>>>> multipolygon = Arrays.asList(area, area2);

		CoverageArea coverageArea = new CoverageArea(multipolygon);

		PdvReqResp pdv = new PdvReqResp();
		pdv.setAddress(address);
		pdv.setCoverageArea(coverageArea);
		pdv.setDocument("14321321238910001");
		pdv.setId(new UUID(1, 1));
		pdv.setOwnerName("José da Silva");
		pdv.setTradingName("Bar da Breja");

		String json = mapper.writeValueAsString(pdv);
		JSONAssert.assertEquals(
				"{\"id\":\"00000000-0000-0001-0000-000000000001\",\"tradingName\":\"Bar da Breja\",\"ownerName\":\"José da Silva\",\"document\":\"14321321238910001\",\"coverageArea\":{\"coordinates\":[[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]],[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]]],\"type\":\"MultiPolygon\"},\"address\":{\"coordinates\":[-40.0,30.0],\"type\":\"Point\"}}",
				json, false);
	}

	@Test
	public void deserialize() throws JsonParseException, JsonMappingException, IOException {
		String json = "{" 
				+ "\"tradingName\": \"Adega da Cerveja - Pinheiros\"," 
				+ "\"ownerName\": \"Zé da Silva\","
				+ "\"document\": \"1432132123891/0001\"," 
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

		ObjectMapper mapper = new ObjectMapper();
		PdvReqResp pdv = mapper.readValue(json, PdvReqResp.class);
		Assert.assertEquals("Adega da Cerveja - Pinheiros", pdv.getTradingName());
		Assert.assertEquals("Point", pdv.getAddress().getType());
		Assert.assertEquals(-46.57421, pdv.getAddress().getCoordinates().get(0), 0.001);
		Assert.assertEquals(-21.785741, pdv.getAddress().getCoordinates().get(1), 0.001);
		Assert.assertEquals(30, pdv.getCoverageArea().getCoordinates().get(0).get(0).get(0).get(0), 0.001);
	}
}
