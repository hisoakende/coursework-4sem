package ru.hisoakende.coursework.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hisoakende.coursework.models.Ticket;
import ru.hisoakende.coursework.services.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<Page<Ticket>> findTickets(
            @RequestParam(value = "departureAirport", required = false) String departureAirport,
            @RequestParam(value = "arrivalAirport", required = false) String arrivalAirport,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "departureTime", required = false) LocalDateTime departureTime,
            @RequestParam(value = "arrivalTime", required = false) LocalDateTime arrivalTime,
            @RequestParam(value = "airline", required = false) String airline,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {

        Page<Ticket> tickets = ticketService.findTickets(
                departureAirport,
                arrivalAirport,
                minPrice,
                maxPrice,
                departureTime,
                arrivalTime,
                airline,
                page,
                size,
                sort
        );
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, List<String>>> getPossibleFilterValues() {
        Map<String, List<String>> filters = ticketService.getPossibleFilterValues();
        return ResponseEntity.ok(filters);
    }
}