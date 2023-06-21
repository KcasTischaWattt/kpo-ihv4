package com.vkusnoitochka.ordermanagment.controllers;

import com.vkusnoitochka.ordermanagment.exceptions.InvalidDataException;
import com.vkusnoitochka.ordermanagment.models.Dish;
import com.vkusnoitochka.ordermanagment.requests.CreateDishRequest;
import com.vkusnoitochka.ordermanagment.responses.DishResponse;
import com.vkusnoitochka.ordermanagment.services.DishService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<String> createDish(@RequestBody CreateDishRequest request) {
        dishService.createDish(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dish created successfully.");
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishResponse> getDish(@PathVariable Integer dishId) {
        Dish dish = dishService.getDish(dishId);
        if (dish != null) {
            DishResponse response = new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), dish.getPrice());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{dishId}")
    public ResponseEntity<String> updateDish(@PathVariable Integer dishId, @RequestBody CreateDishRequest request) {
        try {
            dishService.updateDish(dishId, request);
            return ResponseEntity.ok("Dish updated successfully.");
        } catch (InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<String> deleteDish(@PathVariable Integer dishId) {
        try {
            dishService.deleteDish(dishId);
            return ResponseEntity.ok("Dish deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/menu")
    public ResponseEntity<List<DishResponse>> getMenu() {
        List<Dish> availableDishes = dishService.getAvailableDishes();
        List<DishResponse> menu = availableDishes.stream()
                .map(dish -> new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), dish.getPrice()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(menu);
    }

}

