package com.zx.pdv.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

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
	public PdvReqResp json(@JsonProperty(required=true, value="id") UUID id,
			@JsonProperty(required=true, value="tradingName") String tradingName,
			@JsonProperty(required=true, value="ownerName") String ownerName,
			@JsonProperty(required=true, value="document") String document,
			@JsonProperty(required=true, value="coverageArea") CoverageArea coverageArea,
			@JsonProperty(required=true, value="address") Address address) {
		
		PdvReqResp response = new PdvReqResp();
		response.setAddress(address);
		response.setCoverageArea(coverageArea);
		response.setDocument(document);
		response.setId(id);
		response.setOwnerName(ownerName);
		response.setTradingName(tradingName);
		
		return response;
		
	}
	
}
