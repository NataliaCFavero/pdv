package com.zx.pdv.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.caelum.stella.bean.validation.CNPJ;
import lombok.Data;

@Data
public class PdvReqResp {
	
	private UUID id;
	
	@NotNull
	private String tradingName;
	
	@NotNull
	private String ownerName;
	
	@CNPJ(formatted=true)
	@NotNull
	private String document;
	
	@NotNull
	private CoverageArea coverageArea;
	
	@NotNull
	private Address address;

	@JsonIgnore
	private MultiPolygon multiPolygon;
	
	@JsonIgnore
	private Double distance;
		
	@JsonCreator
	public void json(@JsonProperty(required=true) UUID id,
			@JsonProperty(required=true) String tradingName,
			@JsonProperty(required=true) String ownerName,
			@JsonProperty(required=true) String document,
			@JsonProperty(required=true) CoverageArea coverageArea,
			@JsonProperty(required=true) Address address) {
		
	}
	
}
