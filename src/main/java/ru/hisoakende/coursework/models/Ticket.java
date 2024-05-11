package ru.hisoakende.coursework.models;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime departureDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime arrivalDatetime;

    private String departureAirport;

    private String arrivalAirport;

    private String airline;

    private String airplane;

    private int price;
}