package com.vkusnoitochka.ordermanagment.responses;

public class OrderResponse {
    private Integer orderId;
    private String status;

    public OrderResponse() {
    }

    public OrderResponse(Integer orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
