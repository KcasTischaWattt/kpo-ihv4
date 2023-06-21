package com.vkusnoitochka.ordermanagment.services;

import com.vkusnoitochka.ordermanagment.exceptions.InvalidDataException;
import com.vkusnoitochka.ordermanagment.models.Dish;
import com.vkusnoitochka.ordermanagment.reposiroty.DishRepository;
import com.vkusnoitochka.ordermanagment.requests.CreateDishRequest;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public void createDish(@NotNull CreateDishRequest request) {
        // Создаем новое блюдо
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setQuantity(request.getQuantity());
        if (dish.getQuantity() != 0) {
            dish.setAvailability(true);
        }

        dishRepository.save(dish);
    }

    public Dish getDish(Integer dishId) {
        // Получаем блюдо по его идентификатору
        return dishRepository.findById(dishId).orElse(null);
    }

    public void updateDish(Integer dishId, CreateDishRequest request) throws InvalidDataException, EntityNotFoundException {
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish != null) {
            // Обновляем данные блюда
            dish.setName(request.getName());
            dish.setDescription(request.getDescription());
            dish.setPrice(request.getPrice());
            dish.setQuantity(request.getQuantity());
            if (dish.getQuantity() == 0) {
                dish.setAvailability(false);
            }

            // Сохраняем обновленное блюдо в репозитории
            dishRepository.save(dish);
        } else {
            throw new EntityNotFoundException("Dish not found with ID: " + dishId);
        }
    }

    public void deleteDish(Integer dishId) throws EntityNotFoundException {
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish != null) {
            // Удаляем блюдо из репозитория
            dishRepository.delete(dish);
        } else {
            throw new EntityNotFoundException("Dish not found with ID: " + dishId);
        }
    }

    public List<Dish> getAvailableDishes() {
        return dishRepository.findByAvailability(true);
    }
}

