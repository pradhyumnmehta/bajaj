package com.bajaj.bajaDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check endpoint.
 * GET /health  →  200 OK  { "status": "UP", "timestamp": "..." }
 */
@RestController
@RequestMapping("/health-check")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status",    "UP",
                "service",   "bajaDemo",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
