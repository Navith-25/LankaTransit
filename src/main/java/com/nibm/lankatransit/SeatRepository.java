package com.nibm.lankatransit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByBusId(String busId);
    Optional<Seat> findByBusIdAndSeatNumber(String busId, int seatNumber);
}