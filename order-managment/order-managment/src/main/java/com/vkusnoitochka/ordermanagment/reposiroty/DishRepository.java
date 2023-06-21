package com.vkusnoitochka.ordermanagment.reposiroty;

import com.vkusnoitochka.ordermanagment.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findByAvailability(boolean availability);
}

