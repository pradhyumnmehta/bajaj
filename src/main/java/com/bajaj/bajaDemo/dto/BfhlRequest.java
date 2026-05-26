package com.bajaj.bajaDemo.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request DTO for the /bfhl endpoint.
 * Expects a JSON body: { "data": ["a", "1", "334", ...] }
 */
public class BfhlRequest {

    @NotNull(message = "data field must not be null")
    private List<String> data;

    public BfhlRequest() {}

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
