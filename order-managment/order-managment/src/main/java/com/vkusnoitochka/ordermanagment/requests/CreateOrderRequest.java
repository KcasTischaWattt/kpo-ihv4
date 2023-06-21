package com.vkusnoitochka.ordermanagment.requests;

import com.vkusnoitochka.ordermanagment.models.MenuItem;

import java.util.List;

public class CreateOrderRequest {
    private Integer userId;
    private List<MenuItem> menuItems;
    private String specialRequests;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(List<MenuItem> menuItems, String specialRequests, Integer userId) {
        this.menuItems = menuItems;
        this.specialRequests = specialRequests;
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
}

