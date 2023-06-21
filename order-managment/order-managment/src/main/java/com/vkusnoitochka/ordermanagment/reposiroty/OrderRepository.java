package com.vkusnoitochka.ordermanagment.reposiroty;

import com.vkusnoitochka.ordermanagment.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
