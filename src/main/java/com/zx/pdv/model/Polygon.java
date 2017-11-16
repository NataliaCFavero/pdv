package com.zx.pdv.model;

import java.util.List;

import lombok.Data;

@Data
public class Polygon {
	private final List<Point> exteriorRings;
	private final List<List<Point>> interiorRings;

}
