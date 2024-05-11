package ru.hisoakende.coursework.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hisoakende.coursework.dao.OrderDao;
import ru.hisoakende.coursework.dao.TicketDao;
import ru.hisoakende.coursework.models.Order;
import ru.hisoakende.coursework.models.Ticket;
import ru.hisoakende.coursework.models.User;
import ru.hisoakende.coursework.repositories.UserRepository;
import ru.hisoakende.coursework.requests.OrderRequest;

import java.util.List;


@Service
public class OrderService {

    public final OrderDao orderDao;
    public final TicketDao ticketDao;
    public final UserRepository userRepository;

    public OrderService(OrderDao orderDao, TicketDao ticketDao, UserRepository userRepository) {
        this.orderDao = orderDao;
        this.ticketDao = ticketDao;
        this.userRepository = userRepository;
    }

    public List<Order> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(EntityNotFoundException::new);
        return orderDao.findByUserId(user.getId());
    }

    public void createOrder(OrderRequest orderRequest) {
        Ticket ticket = ticketDao.findById(orderRequest.getTicketId())
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findByUsername(orderRequest.getUsername())
                .orElseThrow(EntityNotFoundException::new);

        Order order = new Order();
        order.setTicket(ticket);
        order.setUser(user);

        orderDao.createOrder(order);
    }
}
