package com.sample.base.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/anonymous/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK anonymous");
    }

    @GetMapping("/token/health-check")
    public ResponseEntity<String> tokenHealthCheck() {
        return ResponseEntity.ok("OK");
    }
}
