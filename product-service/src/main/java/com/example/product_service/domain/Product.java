package com.example.product_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Accessors(chain = true)
public class Product {
    @Id
    private Integer id;
    private String productCode;
    private ProductCategory productCategory;
    private Integer basePrice;
    private Integer currentPrice;
    private Integer availableQuantity;
}
