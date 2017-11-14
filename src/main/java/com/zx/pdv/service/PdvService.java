package com.zx.pdv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.zx.pdv.model.Address;
import com.zx.pdv.model.CoverageArea;
import com.zx.pdv.model.MultiPolygon;
import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.model.Point;
import com.zx.pdv.model.Polygon;
import com.zx.pdv.repository.Pdv;
import com.zx.pdv.repository.PdvRepository;

import rx.Observable;

@Service
@Lazy
public class PdvService {

	private static final String INVALID_DOCUMENT = "Invalid Document";
	private PdvRepository repository;
	private Utils utils;
	
	@Autowired
	public PdvService(PdvRepository repository) {
		utils = new Utils();
		this.repository = Objects.requireNonNull(repository);
	}

	public Observable<PdvReqResp> save(PdvReqResp pdv) {
		
		pdv.setDocument(utils.clearDocument(pdv.getDocument()));
		
		return pdvExists(pdv.getDocument())
				.map(a -> createNewPdv(pdv))
				.flatMap(p -> repository.save(p))
				.map(this::convertToResponse);
	}

	public Observable<PdvReqResp> findById(UUID id) {
		return repository.find(id).map(this::convertToResponse);
	}

	public Observable<PdvReqResp> findPdv(Double x, Double y) {

		Point point = new Point(x, y);

		return repository.find().map(this::createPdvWithMultiPolygon)
				.filter(pdv -> containsPdv(pdv.getMultiPolygon(), point))
				.map(pdv -> calculateDistance(pdv, point))
				.toSortedList((pdv, pdv1) -> compareDistance(pdv, pdv1))
				.flatMap(Observable::from).first();
	}

	private Pdv createNewPdv(PdvReqResp pdv) {
		pdv.setId(UUID.randomUUID());
		return convertToPdv(pdv);
	}

	private static Integer compareDistance(PdvReqResp pdv1, PdvReqResp pdv2) {
		return pdv1.getDistance().compareTo(pdv2.getDistance());
	}

	private PdvReqResp calculateDistance(PdvReqResp pdv, Point point) {
		double distance = Math.hypot(pdv.getAddress().getCoordinates().get(0) - point.getX(),
				pdv.getAddress().getCoordinates().get(1) - point.getY());
		pdv.setDistance(distance);
		return pdv;
	}

	private boolean containsPdv(MultiPolygon multiPolygon, Point point) {

		List<Polygon> polygons = multiPolygon.getPolygons();

		for (int n = 0; n < polygons.size(); n++) {
			boolean extern = false;
			boolean intern = false;

			List<Point> externPolygon = polygons.get(n).getExteriorRings();
			extern = contains(point, externPolygon);

			intern = containsInPolygonIntern(point, polygons.get(n).getInteriorRings());

			if (extern && !intern) {
				return true;
			}
		}

		return false;

	}

	private boolean containsInPolygonIntern(Point point, List<List<Point>> interiorRings) {
		boolean intern = false;
		for (int k = 0; k < interiorRings.size(); k++) {
			List<Point> internPolygon = interiorRings.get(k);
			intern = contains(point, internPolygon);
			if (intern) {
				break;
			}
		}
		return intern;
	}

	private boolean contains(Point point, List<Point> points) {

		boolean result = false;
		for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
			if ((points.get(i).getY() > point.getY()) != (points.get(j).getY() > point.getY())
					&& (point.getX() < (points.get(j).getX() - points.get(i).getX())
							* (point.getY() - points.get(i).getY()) / (points.get(j).getY() - points.get(i).getY())
							+ points.get(i).getX())) {
				result = !result;
			}
		}
		return result;
	}

	private PdvReqResp createPdvWithMultiPolygon(Pdv pdvDao) {
		PdvReqResp pdv = convertToResponse(pdvDao);
		pdv.setMultiPolygon(createMultipolygon(pdvDao));
		return pdv;
	}

	private MultiPolygon createMultipolygon(Pdv pdv) {
		List<Polygon> polygons = new ArrayList<>();
		for (int i = 0; i < pdv.getCoverageArea().size(); i++) {

			polygons.add(createPolygon(pdv.getCoverageArea().get(i)));

		}
		return new MultiPolygon(polygons);
	}

	private Polygon createPolygon(List<List<List<Double>>> polygons) {
		List<Point> pointsConsider = new ArrayList<>();
		List<List<Point>> pointsDisregard = new ArrayList<>();

		if (polygons.size() > 0) {
			pointsConsider = points(polygons.get(0));
		}

		if (polygons.size() > 1) {
			for (int i = 1; i < polygons.size(); i++) {
				pointsDisregard.add(points(polygons.get(i)));
			}

		}

		return new Polygon(pointsConsider, pointsDisregard);

	}

	private List<Point> points(List<List<Double>> polygon) {
		List<Point> points = new ArrayList<>();
		for (int j = 0; j < polygon.size(); j++) {
			Double x = polygon.get(j).get(0);
			Double y = polygon.get(j).get(1);
			Point point = new Point(x, y);
			points.add(point);
		}
		return points;
	}

	private Observable<Boolean> pdvExists(String document) {
		Objects.requireNonNull(document);
		return repository.find(document).isEmpty().doOnNext(x -> Assert.isTrue(x, INVALID_DOCUMENT));

	}

	private PdvReqResp convertToResponse(Pdv pdv) {
		Objects.requireNonNull(pdv);
		PdvReqResp model = new PdvReqResp();

		Address address = new Address();
		address.setCoordinates(pdv.getAddress());

		CoverageArea coverageArea = new CoverageArea();
		coverageArea.setCoordinates(pdv.getCoverageArea());

		model.setAddress(address);
		model.setCoverageArea(coverageArea);
		model.setOwnerName(pdv.getOwnerName());
		model.setTradingName(pdv.getTradingName());
		model.setId(pdv.getId());
		model.setDocument(pdv.getDocument());
		return model;
	}

	private Pdv convertToPdv(PdvReqResp pdv) {
		Objects.requireNonNull(pdv);
		Pdv dao = new Pdv();
		dao.setAddress(pdv.getAddress().getCoordinates());
		dao.setCoverageArea(pdv.getCoverageArea().getCoordinates());
		dao.setId(pdv.getId());
		dao.setOwnerName(pdv.getOwnerName());
		dao.setTradingName(pdv.getTradingName());
		dao.setDocument(pdv.getDocument());
		return dao;
	}

}
