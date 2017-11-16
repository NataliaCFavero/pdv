package com.zx.pdv.repository;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.common.util.concurrent.ListenableFuture;

import rx.Observable;

@Repository
public class PdvRepository {

	Mapper<Pdv> mapper;

	@Autowired
	public PdvRepository(MappingManager manager) {
		this.mapper = Objects.requireNonNull(manager.mapper(Pdv.class));
	}

	public Observable<Pdv> save(Pdv pdv) {
		return Observable.from(mapper.saveAsync(pdv)).map(p -> pdv);
	}

	public Observable<Pdv> find() {
		ResultSetFuture resultSetFuture = mapper.getManager().getSession().executeAsync("Select *from pdv");
		return Observable.from(resultSetFuture).map(mapper::map).map(Result::all).flatMap(Observable::from);
	}

	public Observable<Pdv> find(UUID id) {
		ListenableFuture<Pdv> result = mapper.getAsync(id);
		return Observable.from(result).filter(pdv -> !Objects.isNull(pdv));
	}

	public Observable<Pdv> find(String document) {
		ResultSetFuture result = mapper.getManager().getSession().executeAsync("Select *from pdv where document = ?",
				document);
		return Observable.from(result).map(mapper::map).map(Result::one);
	}

}
