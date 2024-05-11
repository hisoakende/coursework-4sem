package ru.hisoakende.coursework.dao;

import org.springframework.stereotype.Repository;
import ru.hisoakende.coursework.models.Order;
import ru.hisoakende.coursework.repositories.OrderRepository;

import java.util.List;

@Repository
public class OrderDao {

    private final OrderRepository orderRepository;

    public OrderDao(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }
}
