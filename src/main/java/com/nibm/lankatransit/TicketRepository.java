package com.nibm.lankatransit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Find all tickets bought by a specific user
    List<Ticket> findByPassengerUsername(String passengerUsername);
}