package com.nibm.lankatransit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"busId", "seatNumber"})
})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busId;       // e.g., "ND-5555" (Highway Bus)
    private int seatNumber;     // 1, 2, 3... 40

    // Concurrency Handling
    private LocalDateTime lockedUntil;
    private String lockedBy;    // User who currently holds the lock

    private boolean isSold;     // True if payment fully completed

    public Seat() {}

    // Constructor for initialization
    public Seat(String busId, int seatNumber) {
        this.busId = busId;
        this.seatNumber = seatNumber;
        this.isSold = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }
    public String getLockedBy() { return lockedBy; }
    public void setLockedBy(String lockedBy) { this.lockedBy = lockedBy; }
    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }
}