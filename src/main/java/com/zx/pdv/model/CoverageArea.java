package com.zx.pdv.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoverageArea {

	private final String type = "MultiPolygon";
	private List<List<List<List<Double>>>> coordinates;
	
//	@JsonCreator
//	static CoverageArea json(@JsonProperty(required=true, value="type") String type,
//			@JsonProperty(required=true, value="coordinates") List<List<List<List<Double>>>> coordinates) {
//		return new CoverageArea(coordinates);
//	}
}
