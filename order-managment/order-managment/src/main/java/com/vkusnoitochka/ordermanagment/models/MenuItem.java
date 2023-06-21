package com.vkusnoitochka.ordermanagment.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer dishId;
    private Integer quantity;

    public MenuItem() {
    }

    public MenuItem(Integer dishId, Integer quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
