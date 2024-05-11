package ru.hisoakende.coursework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hisoakende.coursework.models.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT DISTINCT t.departureAirport FROM Ticket t")
    List<String> findDistinctDepartureAirports();

    @Query("SELECT DISTINCT t.arrivalAirport FROM Ticket t")
    List<String> findDistinctArrivalAirports();

    @Query("SELECT DISTINCT t.airline FROM Ticket t")
    List<String> findDistinctAirlines();

}
