package ru.hisoakende.coursework.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import ru.hisoakende.coursework.models.Ticket;
import ru.hisoakende.coursework.repositories.TicketRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDao {

    public final TicketRepository ticketRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TicketDao(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Page<Ticket> findTickets(String departureAirport,
                                    String arrivalAirport,
                                    Double minPrice,
                                    Double maxPrice,
                                    LocalDateTime departureTime,
                                    LocalDateTime arrivalTime,
                                    String airline,
                                    int page,
                                    int size,
                                    String sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
        Root<Ticket> root = query.from(Ticket.class);

        List<Predicate> predicates = new ArrayList<>();

        if (departureAirport != null) {
            predicates.add(cb.equal(root.get("departureAirport"), departureAirport));
        }
        if (arrivalAirport != null) {
            predicates.add(cb.equal(root.get("arrivalAirport"), arrivalAirport));
        }
        if (minPrice == null) {
            minPrice = -1d;
        }
        if (maxPrice == null) {
            maxPrice = 1_000_000_000d;
        }
        if (minPrice != -1d || maxPrice != 1_000_000_000d) {
            predicates.add(cb.between(root.get("price"), minPrice, maxPrice));
        }
        if (departureTime != null) {
            predicates.add(cb.equal(root.get("departureDatetime"), departureTime));
        }
        if (arrivalTime != null) {
            predicates.add(cb.equal(root.get("arrivalDatetime"), arrivalTime));
        }
        if (airline != null) {
            predicates.add(cb.equal(root.get("airline"), airline));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        Sort.Direction sortDirection = Sort.DEFAULT_DIRECTION;
        String sortField = "id";

        if (sort != null && !sort.isEmpty()) {
            if (sort.startsWith("-")) {
                sortDirection = Sort.Direction.DESC;
                sortField = sort.substring(1);
            } else {
                sortField = sort;
            }
        }

        Sort sortObject = Sort.by(sortDirection, sortField);

        query.orderBy(QueryUtils.toOrders(sortObject, root, cb));

        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sortField);

        List<Ticket> tickets = entityManager.createQuery(query)
                .setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        return new PageImpl<>(tickets, pageRequest, entityManager.createQuery(query).getResultList().size());
    }

    public List<String> findDistinctDepartureAirports() {
        return ticketRepository.findDistinctDepartureAirports();
    }

    public List<String> findDistinctArrivalAirports() {
        return ticketRepository.findDistinctArrivalAirports();
    }

    public List<String> findDistinctAirlines() {
        return ticketRepository.findDistinctAirlines();
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }
}
