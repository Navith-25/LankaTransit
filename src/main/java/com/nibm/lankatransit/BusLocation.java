package com.nibm.lankatransit;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_location")
public class BusLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busId; // e.g., "ND-1234"

    // This is the special Spatial column
    @Column(columnDefinition = "geometry(Point,4326)")
    // If using MySQL, change above line to: @Column(columnDefinition = "POINT")
    private Point location;

    private LocalDateTime timestamp;

    // Default Constructor
    public BusLocation() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }

    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}