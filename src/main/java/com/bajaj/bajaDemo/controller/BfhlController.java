package com.bajaj.bajaDemo.controller;

import com.bajaj.bajaDemo.dto.BfhlRequest;
import com.bajaj.bajaDemo.dto.BfhlResponse;
import com.bajaj.bajaDemo.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller exposing the /bfhl endpoint.
 */
@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*")
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * GET /bfhl
     * Returns a hardcoded operation code to confirm the endpoint is live.
     * Expected response: { "operation_code": 1 }
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> handleGet() {
        return ResponseEntity.ok(Map.of("operation_code", 1));
    }

    /**
     * POST /bfhl
     * Accepts a JSON body with a "data" array and returns the processed response.
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> handlePost(@Valid @RequestBody BfhlRequest request) {
        BfhlResponse response = bfhlService.process(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Global fallback for validation errors — returns a 400 with is_success: false.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "is_success", false,
                        "error", ex.getMessage() != null ? ex.getMessage() : "Bad request"
                ));
    }
}
