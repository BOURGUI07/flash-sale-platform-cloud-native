package com.example.customer_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderHistory {
    @Id
    private Integer id;
    private Integer customerId;
    private String productCode;
    private ProductCategory productCategory;
    private Integer quantity;
    private Integer price;
    private OrderStatus orderStatus;
    private Instant orderDate;
}
