package com.nibm.lankatransit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    // 1. Initialize Seats (Admin/Owner would run this once per bus)
    @PostMapping("/init")
    public String initBusSeats(@RequestParam String busId, @RequestParam int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) {
            if (seatRepository.findByBusIdAndSeatNumber(busId, i).isEmpty()) {
                seatRepository.save(new Seat(busId, i));
            }
        }
        return "Initialized " + totalSeats + " seats for " + busId;
    }

    // 2. Get Seat Layout (Shows which are available)
    @GetMapping("/{busId}")
    public List<Seat> getBusSeats(@PathVariable String busId) {
        // TODO: In a real app, we would hide 'lockedBy' field here for privacy
        return seatRepository.findByBusId(busId);
    }

    // 3. LOCK A SEAT (The critical concurrency part)
    @PostMapping("/lock")
    public ResponseEntity<String> lockSeat(@RequestBody SeatRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        Optional<Seat> seatOpt = seatRepository.findByBusIdAndSeatNumber(request.getBusId(), request.getSeatNumber());

        if (seatOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Seat does not exist.");
        }

        Seat seat = seatOpt.get();
        LocalDateTime now = LocalDateTime.now();

        // CHECK: Is it sold?
        if (seat.isSold()) {
            return ResponseEntity.status(409).body("Seat is already SOLD.");
        }

        // CHECK: Is it locked by someone else right now?
        // It is locked IF (lockedUntil is not null) AND (lockedUntil > now)
        if (seat.getLockedUntil() != null && seat.getLockedUntil().isAfter(now)) {
            if (!seat.getLockedBy().equals(currentUser)) {
                return ResponseEntity.status(409).body("Seat is temporarily LOCKED by another user.");
            }
        }

        // LOCK IT: Set lock for 5 minutes
        seat.setLockedBy(currentUser);
        seat.setLockedUntil(now.plusMinutes(5));
        seatRepository.save(seat);

        return ResponseEntity.ok("Seat " + request.getSeatNumber() + " locked for 5 minutes. Proceed to payment.");
    }
}

class SeatRequest {
    private String busId;
    private int seatNumber;
    // Getters/Setters
    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}