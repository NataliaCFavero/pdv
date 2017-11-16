package com.zx.pdv.template;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.zx.pdv.model.Address;
import com.zx.pdv.model.CoverageArea;
import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.repository.Pdv;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PdvTemplate implements TemplateLoader {

	@Override
	public void load() {
		
		Fixture.of(PdvReqResp.class).addTemplate("complete", new Rule() {
			{
				add("id", UUID.fromString("d82e8b25-92a7-43c5-9bea-29b672bedbe3"));
				add("document", "43.362.985/0001-08");
				add("ownerName", "Zé da Silva");
				add("tradingName", "Adega da Cerveja - Pinheiros");
				add("address", one(Address.class, "valid"));
				add("coverageArea", one(CoverageArea.class, "with2AreasValid"));
			}
		});
		
		Fixture.of(PdvReqResp.class).addTemplate("invalidCnpj").inherits("complete", new Rule() {
			{
				add("document", "11.111.111/0001-10");
			}
		});
		
		Fixture.of(Address.class).addTemplate("valid", new Rule() {
			{
				add("coordinates", Arrays.asList(-46.57421, -21.785741));
			}
		});

		Fixture.of(CoverageArea.class).addTemplate("with2AreasValid", new Rule() {
			{
				add("coordinates", with2AreasValid());
			}
		});
		
		Fixture.of(Pdv.class).addTemplate("valid", new Rule() {{
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

			add("id", new UUID(1,1));
			add("document", "08399697000127");
			add("ownerName", "José Maria");
			add("tradingName", "Adega da Cerveja - Pinheiros");
			add("address", createPoint(-12.0, -45.0));
			add("coverageArea", multipolygon);
		}});
		
		Fixture.of(Pdv.class).addTemplate("invalid", new Rule() {{

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

			add("id", new UUID(2,2));
			add("document", "08399697000127");
			add("ownerName", "Maria José");
			add("tradingName", "Pdv sem area de cobertura");
			add("address", createPoint(-12.0, -45.0));
			add("coverageArea", multipolygonA);
		}});
	}

	private List<List<List<List<Double>>>> with2AreasValid() {
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

		return Arrays.asList(areaDao, area2Dao);
	}
	
	private List<Double> createPoint(double lat, double lng) {
		return Arrays.asList(lat, lng);
	}

}
