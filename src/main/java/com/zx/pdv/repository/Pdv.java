package com.zx.pdv.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Indexed;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import lombok.Data;

@Data
@Table(name = "pdv")
public class Pdv {
	@PartitionKey
	private UUID id;
	@Indexed
	private String document;
	private String tradingName;
	private String ownerName;
	private List<List<List<List<Double>>>> coverageArea;
	private List<Double> address;

	
}
