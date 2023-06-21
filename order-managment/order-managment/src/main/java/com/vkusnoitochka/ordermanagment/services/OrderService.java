package com.vkusnoitochka.ordermanagment.services;

import com.vkusnoitochka.ordermanagment.exceptions.InvalidDataException;
import com.vkusnoitochka.ordermanagment.models.Dish;
import com.vkusnoitochka.ordermanagment.models.MenuItem;
import com.vkusnoitochka.ordermanagment.models.Order;
import com.vkusnoitochka.ordermanagment.reposiroty.DishRepository;
import com.vkusnoitochka.ordermanagment.reposiroty.OrderRepository;
import com.vkusnoitochka.ordermanagment.requests.CreateOrderRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
    }

    public Order createOrder(@NotNull CreateOrderRequest request) throws InvalidDataException {
        // Проверяем правильность предоставленных данных
        if (request.getUserId() == null || request.getMenuItems().isEmpty()) {
            throw new InvalidDataException("Invalid order data. User ID and menu items are required.");
        }

        // Проверяем, есть ли блоюда в меню
        if (dishRepository.count() == 0) {
            throw new InvalidDataException("No dishes found in the menu.");
        }

        // Создаем новый заказ
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("в ожидании");
        order.setSpecialRequests(request.getSpecialRequests());
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Проверяем каждое блюдо в заказе
        for (MenuItem item : request.getMenuItems()) {
            // Проверяем наличие блюда в меню
            Optional<Dish> dishOptional = dishRepository.findById(item.getDishId());
            Dish dish = dishOptional.orElseThrow(() -> new InvalidDataException("Invalid order data. Dish with ID " + item.getDishId() + " does not exist in the menu."));

            // Проверяем достаточность количества блюд
            if (item.getQuantity() > dish.getQuantity()) {
                throw new InvalidDataException("Invalid order data. Dish with ID " + item.getDishId() + " has insufficient quantity in the menu.");
            }

            // Уменьшаем количество блюд в меню
            dish.setQuantity(dish.getQuantity() - item.getQuantity());
            dishRepository.save(dish); // Сохраняем обновленное количество блюд
        }

        // Сохраняем заказ в репозитории
        Order savedOrder = orderRepository.save(order);

        // Если заказ был успешно сохранен, меняем его статус на "выполнен"
        if (savedOrder != null) {
            // Обработка заказа
            // Изменение статуса на "выполнен"
            savedOrder.setStatus("выполнен");
            orderRepository.save(savedOrder); // Сохранение обновленного заказа
        }

        return savedOrder;
    }

    public Order getOrder(Integer orderId) {
        // Получаем заказ по его идентификатору
        return orderRepository.findById(orderId).orElse(null);
    }
}

