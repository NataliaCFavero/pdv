package com.zx.pdv.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

@RunWith(MockitoJUnitRunner.class)
public class PdvRepositoryTest {

	private PdvRepository repository;
	private Mapper<Pdv> mapper;
	private MappingManager manager;

	@Rule
	public CassandraCQLUnit cassandra = new CassandraCQLUnit(new ClassPathCQLDataSet("SCRIPT_PDV.cql", "pdv"));
	
	@Before
	public void setUp() {
		manager = new MappingManager(cassandra.session);
		mapper = manager.mapper(Pdv.class);
		repository = new PdvRepository(manager);
		FixtureFactoryLoader.loadTemplates("com.zx.pdv.template");
	}
	
	@BeforeClass
	public static void init() throws ConfigurationException, TTransportException, IOException, InterruptedException {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra(10000);
	}
	
	@Test
	public void testSavePdvSuccess() {			
		Pdv pdv = Fixture.from(Pdv.class).gimme("valid");
		
		repository.save(pdv).toBlocking().first();
		
		Pdv findPdv = mapper.get(new UUID(1, 1));
		assertEquals("José Maria", findPdv.getOwnerName());
	}
	
	@Test
	public void testFindAllPdv() {
		List<Pdv> pdvs = repository.find().toList().toBlocking().single();
		Assert.assertEquals(3, pdvs.size());
	}
	
	@Test
	public void testFindPdvById() {
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
		
		List<List<List<List<Double>>>> coverageArea = Arrays.asList(area, area2);
		
		UUID id = UUID.fromString("53d9f5b3-f08b-4118-8434-107dddb6e69e");
		Pdv pdv = repository.find(id).toBlocking().single();
		assertNotNull(pdv);
		assertEquals("11607462000198", pdv.getDocument());
		assertEquals(coverageArea, pdv.getCoverageArea());
		assertEquals(Arrays.asList(40.0, 30.0), pdv.getAddress());
		assertEquals(id, pdv.getId());
		assertEquals("José Maria José", pdv.getOwnerName());
		assertEquals("Cervejaria da Skina", pdv.getTradingName());
	}
	
	@Test
	public void testNotFoundPdvById() {
		assertTrue(repository.find(new UUID(34,22)).toList().toBlocking().single().isEmpty());	
	}
	
	@Test
	public void testFindPdvByCnpj() throws InterruptedException, ExecutionException {
		Pdv pdv = repository.find("11607462000198").toBlocking().single();
		assertNotNull(pdv);
		assertEquals("Cervejaria da Skina", pdv.getTradingName());
	}
	
	@Test
	public void testNotFoundPdvByCnpj() throws InterruptedException, ExecutionException {
		Pdv pdv = repository.find("11111").toBlocking().single();
		assertNull(pdv);
	}
	
}
