package com.bajaj.bajaDemo.service;

import com.bajaj.bajaDemo.dto.BfhlRequest;
import com.bajaj.bajaDemo.dto.BfhlResponse;

/**
 * Service interface for BFHL business logic.
 */
public interface BfhlService {

    /**
     * Processes the incoming data array and returns a populated BfhlResponse.
     *
     * @param request the incoming request containing the data array
     * @return a fully populated BfhlResponse
     */
    BfhlResponse process(BfhlRequest request);
}
