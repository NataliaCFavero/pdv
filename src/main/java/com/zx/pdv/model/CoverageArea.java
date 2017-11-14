package com.zx.pdv.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoverageArea {

	private final String type = "MultiPolygon";
	private List<List<List<List<Double>>>> coordinates;
}
