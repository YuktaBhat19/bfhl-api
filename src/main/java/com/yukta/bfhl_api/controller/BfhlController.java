package com.yukta.bfhl_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.yukta.bfhl_api.dto.RequestDto;
import com.yukta.bfhl_api.dto.ResponseDto;
import com.yukta.bfhl_api.service.BfhlService;

@RestController
@RequestMapping("/")
public class BfhlController {

    @Autowired
    private BfhlService service;

    @PostMapping("/bfhl")
    public ResponseEntity<ResponseDto> process(
            @RequestBody RequestDto request,
            @RequestHeader("X-Request-Id") String requestId) {

        return ResponseEntity.ok(
                service.process(request, requestId));
    }
    @GetMapping("/test")
    public String test() {
        return "API Working";
    }
}