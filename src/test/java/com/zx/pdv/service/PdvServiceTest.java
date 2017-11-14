package com.zx.pdv.service;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.zx.pdv.model.Address;
import com.zx.pdv.model.CoverageArea;
import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.repository.Pdv;
import com.zx.pdv.repository.PdvRepository;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class PdvServiceTest {

	@InjectMocks
	private PdvService service;

	@Mock
	private PdvRepository repository;
	
	@Before
	public void setUp() {
		service = new PdvService(repository);
	}

	@Test
	public void testSaveSuccess() throws InterruptedException, ExecutionException {
		String document = "76.732.228/0001-38";
		Pdv pdvRepository = new Pdv();

		List<Double> point1 = Arrays.asList(30.00, 20.00);
		List<Double> point2 = Arrays.asList(45.00, 40.00);
		List<Double> point3 = Arrays.asList(10.00, 40.00);
		List<Double> point4 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygonDao = Arrays.asList(point1, point2, point3, point4);
		List<List<List<Double>>> areaDao = Arrays.asList(polygonDao);

		List<Double> point11 = Arrays.asList(30.00, 20.00);
		List<Double> point22 = Arrays.asList(45.00, 40.00);
		List<Double> point33 = Arrays.asList(10.00, 40.00);
		List<Double> point44 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygon2Dao = Arrays.asList(point11, point22, point33, point44);
		List<List<List<Double>>> area2Dao = Arrays.asList(polygon2Dao);

		List<List<List<List<Double>>>> multipolygon = Arrays.asList(areaDao, area2Dao);

		pdvRepository.setId(new UUID(1, 1));
		pdvRepository.setDocument("76732228000138");
		pdvRepository.setOwnerName("Zé da Silva");
		pdvRepository.setTradingName("Adega da Cerveja - Pinheiros");
		pdvRepository.setAddress(Arrays.asList(-46.57421, -21.785741));
		pdvRepository.setCoverageArea(multipolygon);

		Address address = new Address(pdvRepository.getAddress());
		CoverageArea coverageArea = new CoverageArea(pdvRepository.getCoverageArea());

		PdvReqResp pdv = new PdvReqResp();
		pdv.setAddress(address);
		pdv.setCoverageArea(coverageArea);
		pdv.setDocument(document);
		pdv.setOwnerName("José da Silva");
		pdv.setTradingName("Bar da Breja");

		Mockito.when(repository.find("76732228000138")).thenReturn(Observable.empty());
		Mockito.when(repository.save(Mockito.any())).thenReturn(Observable.just(pdvRepository));
				
		PdvReqResp pdvResult = service.save(pdv).toBlocking().single();
		assertNotNull(pdvResult);
		assertNotNull(pdvResult.getId());
	}
	
//	@Test(expected = IllegalArgumentException.class)
//	public void testCnpjInvalid() throws InterruptedException, ExecutionException {
//		PdvResponse pdv = new PdvResponse();
//		pdv.setDocument("1111111111111");
//
//		service.save(pdv).toBlocking().single();
//		Mockito.verify(repository.find("1111111111111"), Mockito.times(0));
//	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCnpjExists() throws InterruptedException, ExecutionException {
		String document = "73.174.319/0001-07";
		
		List<Double> point1 = Arrays.asList(30.00, 20.00);
		List<Double> point2 = Arrays.asList(45.00, 40.00);
		List<Double> point3 = Arrays.asList(10.00, 40.00);
		List<Double> point4 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygonDao = Arrays.asList(point1, point2, point3, point4);
		List<List<List<Double>>> areaDao = Arrays.asList(polygonDao);

		List<Double> point11 = Arrays.asList(30.00, 20.00);
		List<Double> point22 = Arrays.asList(45.00, 40.00);
		List<Double> point33 = Arrays.asList(10.00, 40.00);
		List<Double> point44 = Arrays.asList(30.00, 20.00);
		List<List<Double>> polygon2Dao = Arrays.asList(point11, point22, point33, point44);
		List<List<List<Double>>> area2Dao = Arrays.asList(polygon2Dao);

		List<List<List<List<Double>>>> multipolygon = Arrays.asList(areaDao, area2Dao);
		
		Pdv pdvExists = new Pdv();
		pdvExists.setId(new UUID(1, 1));
		pdvExists.setDocument(document);
		pdvExists.setOwnerName("Zé da Silva");
		pdvExists.setTradingName("Adega da Cerveja - Pinheiros");
		pdvExists.setAddress(Arrays.asList(-46.57421, -21.785741));
		pdvExists.setCoverageArea(multipolygon);
		
		Address address = new Address(Arrays.asList(-40.0, 30.0));
		CoverageArea coverageArea = new CoverageArea(multipolygon);

		PdvReqResp pdv = new PdvReqResp();
		pdv.setAddress(address);
		pdv.setCoverageArea(coverageArea);
		pdv.setDocument(document);
		pdv.setOwnerName("José da Silva");
		pdv.setTradingName("Bar da Breja");
		
		Mockito.when(repository.find("73174319000107")).thenReturn(Observable.just(pdvExists));
						
		PdvReqResp pdvResult = service.save(pdv).toBlocking().single();
		assertNotNull(pdvResult);
		assertNotNull(pdvResult.getId());
		Mockito.verify(repository.save(Mockito.any()), Mockito.times(0));
	}

	@Test
	public void testFindById() {
		UUID id = UUID.fromString("d82e8b25-92a7-43c5-9bea-29b672bedbe3");

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

		Pdv pdvDao = new Pdv();
		pdvDao.setId(id);
		pdvDao.setDocument("08399697000127");
		pdvDao.setOwnerName("José Maria");
		pdvDao.setTradingName("Adega da Cerveja - Pinheiros");
		pdvDao.setAddress(Arrays.asList(-46.57421, -21.785741));
		pdvDao.setCoverageArea(multipolygon);
		
		Mockito.when(repository.find(id)).thenReturn(Observable.just(pdvDao));
		PdvReqResp pdv = service.findById(id).toBlocking().single();
		Assert.assertEquals("José Maria", pdv.getOwnerName());
		Assert.assertEquals("Point", pdv.getAddress().getType());
		Assert.assertEquals("08399697000127", pdv.getDocument());
	}
	
	@Test
	public void testNotFoundPdvById() {
		UUID id = new UUID(1, 1);
		Mockito.when(repository.find(id)).thenReturn(Observable.empty());
		Assert.assertTrue(service.findById(id).toList().toBlocking().single().isEmpty());
	}

	@Test
	public void testFindCloserPdv() {
		List<Double> point1 = createPoint(40.00, 40.00);
		List<Double> point2 = createPoint(20.00, 45.00);
		List<Double> point3 = createPoint(45.00, 30.00);
		List<Double> point4 = createPoint(40.00, 40.00);
		List<List<Double>> polygon = Arrays.asList(point1, point2, point3, point4);
		List<List<List<Double>>> area = Arrays.asList(polygon);

		List<Double> point11 = createPoint(20.00, 35.00);
		List<Double> point22 = createPoint(10.00, 30.00);
		List<Double> point33 = createPoint(10.00, 10.00);
		List<Double> point44 = createPoint(30.00, 5.00);
		List<Double> point55 = createPoint(45.00, 20.00);
		List<Double> point66 = createPoint(20.00, 35.00);
		List<List<Double>> polygonExtern = Arrays.asList(point11, point22, point33, point44, point55, point66);
		
		List<Double> point111 = createPoint(30.00, 20.00);
		List<Double> point222 = createPoint(20.00, 15.00);
		List<Double> point333 = createPoint(20.00, 25.00);
		List<Double> point444 = createPoint(30.00, 20.00);
		List<List<Double>> polygonIntern = Arrays.asList(point111, point222, point333, point444);
		List<List<List<Double>>> area2 = Arrays.asList(polygonExtern, polygonIntern);

		List<List<List<List<Double>>>> multipolygon = Arrays.asList(area, area2);

		Pdv pdvDaoCorrect = new Pdv();
		pdvDaoCorrect.setId(new UUID(1,1));
		pdvDaoCorrect.setDocument("08399697000127");
		pdvDaoCorrect.setOwnerName("José Maria");
		pdvDaoCorrect.setTradingName("Adega da Cerveja - Pinheiros");
		pdvDaoCorrect.setAddress(createPoint(20.00, 15.00));
		pdvDaoCorrect.setCoverageArea(multipolygon);
		
		List<Double> pointA1 = createPoint(30.00, 20.00);
		List<Double> pointA2 = createPoint(45.00, 40.00);
		List<Double> pointA3 = createPoint(10.00, 40.00);
		List<Double> pointA4 = createPoint(30.00, 20.00);
		List<List<Double>> polygonA = Arrays.asList(pointA1, pointA2, pointA3, pointA4);
		List<List<List<Double>>> areaA = Arrays.asList(polygonA);

		List<Double> pointA11 = createPoint(-15.00, -5.00);
		List<Double> pointA22 = createPoint(-40.00, -10.00);
		List<Double> pointA33 = createPoint(-10.00, -20.00);
		List<Double> pointA44 = createPoint(-5.00, -10.00);
		List<Double> pointA55 = createPoint(-15.00, -5.00);
		List<List<Double>> polygonA2 = Arrays.asList(pointA11, pointA22, pointA33, pointA44, pointA55);
		
		List<List<List<Double>>> areaA2 = Arrays.asList(polygonA2);

		List<List<List<List<Double>>>> multipolygonA = Arrays.asList(areaA, areaA2);

		Pdv pdvDaoIncorrect = new Pdv();
		pdvDaoIncorrect.setId(new UUID(1,1));
		pdvDaoIncorrect.setDocument("08399697000127");
		pdvDaoIncorrect.setOwnerName("José Maria");
		pdvDaoIncorrect.setTradingName("Errado da Cerveja - Pinheiros");
		pdvDaoIncorrect.setAddress(createPoint(-15.0, -5.0));
		pdvDaoIncorrect.setCoverageArea(multipolygonA);
		
		List<Pdv> listPdvs = Arrays.asList(pdvDaoCorrect, pdvDaoIncorrect);
		Mockito.when(repository.find()).thenReturn(Observable.from(listPdvs));
		PdvReqResp pdv = service.findPdv(25.0, 25.0).toBlocking().single();
		Assert.assertEquals("Adega da Cerveja - Pinheiros", pdv.getTradingName());
	}

	private List<Double> createPoint(double lat, double lng) {
		return Arrays.asList(lat, lng);
	}
	
	@Test
	public void testNotFoundCloserPdv() {
		
		
		
	}
}
