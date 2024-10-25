package com.example.aggregator_service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedCustomerInformationResponse(
        List<CustomerInformation> customerInformation,
        Long count
) {
}
