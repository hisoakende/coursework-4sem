package ru.hisoakende.coursework.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.hisoakende.coursework.dao.TicketDao;
import ru.hisoakende.coursework.models.Ticket;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    private final TicketDao ticketDao;

    public TicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public Page<Ticket> findTickets(String departureAirport, String arrivalAirport,
                                    Double minPrice,  Double maxPrice,
                                    LocalDateTime departureTime,  LocalDateTime arrivalTime,
                                    String airline, int page, int size, String sort) {
        return ticketDao.findTickets(departureAirport, arrivalAirport, minPrice,
                maxPrice,
                departureTime,
                arrivalTime,
                airline,
                page,
                size,
                sort
        );
    }

    public Map<String, List<String>> getPossibleFilterValues() {
        Map<String, List<String>> filters = new HashMap<>();

        filters.put("departureAirport", ticketDao.findDistinctDepartureAirports());
        filters.put("arrivalAirport", ticketDao.findDistinctArrivalAirports());
        filters.put("airline", ticketDao.findDistinctAirlines());

        return filters;
    }
}
