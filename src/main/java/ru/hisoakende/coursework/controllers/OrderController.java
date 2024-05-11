package ru.hisoakende.coursework.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.hisoakende.coursework.models.Order;
import ru.hisoakende.coursework.requests.OrderRequest;
import ru.hisoakende.coursework.requests.TicketId;
import ru.hisoakende.coursework.services.OrderService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> findOrders() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(orderService.findByUsername("aboba1"));
    }

    @PostMapping
    public ResponseEntity<?> makeOrder(@RequestBody TicketId ticketId) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUsername(ticketId.getUsername());
        orderRequest.setTicketId(ticketId.getTicketId());
        orderService.createOrder(orderRequest);
        return ResponseEntity.noContent().build();
    }
}
