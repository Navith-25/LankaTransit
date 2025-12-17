package com.nibm.lankatransit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<BusLocation, Long> {

    // Custom query to find buses near a user (Example: within 0.1 degrees ~ 10km)
    // For PostgreSQL/PostGIS:
    @Query(value = "SELECT * FROM bus_location WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326), :distance)", nativeQuery = true)
    List<BusLocation> findNearbyBuses(double lat, double lon, double distance);
}