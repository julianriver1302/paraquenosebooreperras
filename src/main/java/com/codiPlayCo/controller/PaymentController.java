package com.codiPlayCo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codiPlayCo.dto.PaymentRequest;
import com.codiPlayCo.service.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/create-payment-intent")
	public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
	    return ResponseEntity.ok(paymentService.createPaymentIntent(request));
	}


	 
}
