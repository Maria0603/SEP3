package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;

public class OrderByIdRequestDto {

    @NotNull(message = "Order ID is required.")
    private String orderId;

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}