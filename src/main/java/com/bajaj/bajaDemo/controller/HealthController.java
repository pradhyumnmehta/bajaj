package com.bajaj.bajaDemo.controller;

import com.bajaj.bajaDemo.config.UserConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check endpoint.
 * GET /health  →  200 OK
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        // LinkedHashMap to preserve insertion order in JSON response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status",      "UP");
        body.put("service",     "bajaDemo");
        body.put("user_id",     UserConstants.USER_ID);
        body.put("email",       UserConstants.EMAIL);
        body.put("roll_number", UserConstants.ROLL_NUMBER);
        body.put("timestamp",   LocalDateTime.now().toString());
        return ResponseEntity.ok(body);
    }
}
