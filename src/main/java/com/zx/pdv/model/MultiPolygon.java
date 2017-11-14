package com.zx.pdv.model;

import java.util.List;

import lombok.Data;

@Data
public class MultiPolygon {

	private final List<Polygon> polygons;
}
