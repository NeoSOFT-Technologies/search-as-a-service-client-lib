package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.throttler.ThrottlerMaxRequestSizeResponseDTO;
import com.searchclient.clientwrapper.domain.dto.throttler.ThrottlerRateLimitResponseDTO;

public interface ThrottlerServicePort {
	// Rate limiter- throttling ports
	public ThrottlerRateLimitResponseDTO dataInjectionRateLimiter();
	
	// Max Request Size- throttling ports
	public ThrottlerMaxRequestSizeResponseDTO applyDataInjectionRequestSizeLimiter(
			ThrottlerMaxRequestSizeResponseDTO throttlerMaxRequestSizeResponseDTO);
	public ThrottlerMaxRequestSizeResponseDTO dataInjectionRequestSizeLimiter(
			String incomingData);
	public boolean isRequestSizeExceedingLimit(
			ThrottlerMaxRequestSizeResponseDTO throttlerMaxRequestSizeResponseDTO);
}