package ru.hisoakende.coursework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hisoakende.coursework.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}

