package com.zx.pdv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;

@Configuration
public class PdvConfiguration {

	@Bean
	public Cluster cluster(@Value("${cassandra.cluster}") String cluster,
			@Value("${cassandra.address}") String[] address, @Value("${cassandra.port}") Integer port,
			@Value("${cassandra.username}") String username, @Value("${cassandra.password}") String password) {
		return Cluster.builder().withClusterName(cluster).addContactPoints(address).withPort(port)
				.withCredentials(username, password).build();

	}
	
	@Bean
	@Lazy
	public MappingManager mapping(Cluster cluster, @Value("${cassandra.keyspace}") String keyspace) {
		Session session = cluster.connect(keyspace);
		return new MappingManager(session);
	}
	
}
