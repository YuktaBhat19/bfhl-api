package com.yukta.bfhl_api.service;

import com.yukta.bfhl_api.dto.RequestDto;
import com.yukta.bfhl_api.dto.ResponseDto;

public interface BfhlService {

    ResponseDto process(RequestDto request,
                        String requestId);
}