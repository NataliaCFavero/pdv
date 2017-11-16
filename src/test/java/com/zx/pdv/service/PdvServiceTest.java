package com.zx.pdv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.repository.Pdv;
import com.zx.pdv.repository.PdvRepository;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
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
		FixtureFactoryLoader.loadTemplates("com.zx.pdv.template");
	}

	@Test
	public void testSaveSuccess() throws InterruptedException, ExecutionException {
		Pdv pdvRepository = Fixture.from(Pdv.class).gimme("valid");
		PdvReqResp pdv = Fixture.from(PdvReqResp.class).gimme("complete");

		when(repository.find("43362985000108")).thenReturn(Observable.empty());
		when(repository.save(Mockito.any())).thenReturn(Observable.just(pdvRepository));

		PdvReqResp pdvResult = service.save(pdv).toBlocking().single();
		assertNotNull(pdvResult);
		assertNotNull(pdvResult.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveCnpjExists() throws InterruptedException, ExecutionException {
		
		Pdv pdvExists = Fixture.from(Pdv.class).gimme("valid");
		
		PdvReqResp pdv = Fixture.from(PdvReqResp.class).gimme("complete");

		when(repository.find("43362985000108")).thenReturn(Observable.just(pdvExists));

		service.save(pdv).toBlocking().single();
		verify(repository.save(Mockito.any()), times(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCnpj() {
		PdvReqResp pdv = Fixture.from(PdvReqResp.class).gimme("invalidCnpj");
		service.save(pdv).toBlocking().single();
		fail();
	}

	@Test
	public void testFindById() {
		UUID id = new UUID(1,1);
		Pdv pdv = Fixture.from(Pdv.class).gimme("valid");

		Mockito.when(repository.find(id)).thenReturn(Observable.just(pdv));
		PdvReqResp pdvResp = service.findById(id).toBlocking().single();
		assertEquals("José Maria", pdvResp.getOwnerName());
		assertEquals("Point", pdvResp.getAddress().getType());
		assertEquals("08399697000127", pdvResp.getDocument());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotFoundPdvById() {
		UUID id = new UUID(1, 1);
		when(repository.find(id)).thenReturn(Observable.empty());
		service.findById(id).toBlocking().single();
	}

	@Test
	public void testFindCloserPdv() {
		Pdv pdvWithCoverageArea = Fixture.from(Pdv.class).gimme("valid");
		Pdv pdvWithoutCoverageArea = Fixture.from(Pdv.class).gimme("invalid");

		List<Pdv> listPdvs = Arrays.asList(pdvWithCoverageArea, pdvWithoutCoverageArea);
		Mockito.when(repository.find()).thenReturn(Observable.from(listPdvs));
		PdvReqResp pdv = service.findCloserPdv(30.0, 20.0).toBlocking().single();
		assertEquals("Adega da Cerveja - Pinheiros", pdv.getTradingName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotFoundCloserPdv() {
		Pdv pdvWithCoverageArea = Fixture.from(Pdv.class).gimme("valid");
		Pdv pdvWithoutCoverageArea = Fixture.from(Pdv.class).gimme("invalid");

		List<Pdv> listPdvs = Arrays.asList(pdvWithCoverageArea, pdvWithoutCoverageArea);
		Mockito.when(repository.find()).thenReturn(Observable.from(listPdvs));
		PdvReqResp pdv = service.findCloserPdv(300.0, 200.0).toBlocking().single();
		assertEquals("Adega da Cerveja - Pinheiros", pdv.getTradingName());

	}
}
