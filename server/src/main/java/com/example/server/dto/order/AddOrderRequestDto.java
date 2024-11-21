// AddOrderRequestDto.java
package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;

public class AddOrderRequestDto {

    @NotNull(message = "User ID is required.")
    private String userId;

    @NotNull(message = "Offer ID is required.")
    private String offerId;

    @NotNull(message = "Quantity is required.")
    private int quantity;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}