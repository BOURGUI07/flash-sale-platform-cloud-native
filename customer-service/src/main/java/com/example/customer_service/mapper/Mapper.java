package com.example.customer_service.mapper;

import com.example.customer_service.domain.Customer;
import com.example.customer_service.domain.OrderHistory;
import com.example.customer_service.dto.*;

import java.util.List;

public class Mapper {
    public static OrderSummary toOrderSummary(OrderHistory order) {
        return OrderSummary.builder()
                .quantity(order.getQuantity())
                .orderDate(order.getOrderDate())
                .price(order.getPrice())
                .productCode(order.getProductCode())
                .status(order.getOrderStatus())
                .build();
    }

    public static CustomerInformation toCustomerInformation(Customer customer, List<OrderSummary> orders) {
        return CustomerInformation.builder()
                .customerId(customer.getId())
                .name(customer.getName())
                .balance(customer.getBalance())
                .shippingAddress(customer.getShippingAddress())
                .orderSummaries(orders)
                .build();
    }

    public static Customer toCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .name(customerRequest.name())
                .balance(customerRequest.balance())
                .shippingAddress(customerRequest.shippingAddress())
                .build();
    }

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getId())
                .name(customer.getName())
                .balance(customer.getBalance())
                .shippingAddress(customer.getShippingAddress())
                .build();
    }

    public static PurchaseResponse toPurchaseResponse(Customer customer, OrderHistory orderHistory) {
        return PurchaseResponse.builder()
                .customerId(customer.getId())
                .quantity(orderHistory.getQuantity())
                .orderDate(orderHistory.getOrderDate())
                .price(orderHistory.getPrice())
                .productCode(orderHistory.getProductCode())
                .balance(customer.getBalance())
                .shippingAddress(customer.getShippingAddress())
                .customerName(customer.getName())
                .orderId(orderHistory.getId())
                .orderStatus(orderHistory.getOrderStatus())
                .productCategory(orderHistory.getProductCategory())
                .totalPrice(orderHistory.getPrice()*orderHistory.getQuantity())
                .build();
    }

    public static CancelPurchaseResponse toCancelPurchaseResponse(Customer customer, OrderHistory orderHistory) {
        return CancelPurchaseResponse.builder()
                .customerId(customer.getId())
                .orderId(orderHistory.getId())
                .orderStatus(orderHistory.getOrderStatus())
                .balance(customer.getBalance())
                .orderDate(orderHistory.getOrderDate())
                .productCode(orderHistory.getProductCode())
                .returnedQuantity(orderHistory.getQuantity())
                .build();
    }
}
