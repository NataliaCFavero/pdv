package com.zx.pdv.controller;

import java.util.Objects;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zx.pdv.model.ErrorResponse;
import com.zx.pdv.model.PdvReqResp;
import com.zx.pdv.service.PdvService;

@RestController
@RequestMapping(value = "/pdv", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PdvController {

	private PdvService service;
	private static final Logger LOGGER = LoggerFactory.getLogger(PdvController.class);

	@Autowired
	public PdvController(PdvService service) {
		this.service = Objects.requireNonNull(service);
	}

	@PostMapping
	@ResponseBody
	public PdvReqResp create(@RequestBody @Valid final PdvReqResp pdv) {
		return service.save(pdv).doOnNext(this::trace).toSingle().doOnSuccess(this::success).toBlocking().value();
	}
	
	@GetMapping(value = "/{id}")
	@ResponseBody
	public PdvReqResp find(@PathVariable UUID id) {
		return service.findById(id).doOnNext(this::trace).toSingle().doOnSuccess(this::success).toBlocking().value();
		
	}
	
	@GetMapping(value = "/{lat}/{lng}")
	public PdvReqResp findCloserPdv(@PathVariable Double lat, @PathVariable Double lng) { 
		return service.findCloserPdv(lat, lng).doOnNext(this::trace).toSingle().doOnSuccess(this::success).toBlocking().value();
		
	}

	private void trace(PdvReqResp pdv) {
		LOGGER.debug("{}", pdv);
	}

	private void success(PdvReqResp pdv) {
		LOGGER.info("Success: {}", pdv);
	}

	@ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
	@ResponseBody
	public ResponseEntity<ErrorResponse> handle(Exception e) {
		LOGGER.info("Error! >> {}", e);
		ErrorResponse result = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

}
