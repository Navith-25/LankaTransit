package com.nibm.lankatransit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import java.time.LocalDateTime;

@Controller
public class LocationController {

    @Autowired
    private BusRepository busRepository;

    // Driver sends JSON data here: /app/bus.update
    @MessageMapping("/bus.update")
    // Passengers listen here: /topic/public
    @SendTo("/topic/public")
    public BusLocation broadcastLocation(LocationDTO dto) {
        // 1. Convert simple Lat/Lng to Geometry Point
        GeometryFactory geomFactory = new GeometryFactory();
        BusLocation bus = new BusLocation();
        bus.setBusId(dto.getBusId());
        bus.setLocation(geomFactory.createPoint(new Coordinate(dto.getLng(), dto.getLat())));
        bus.setTimestamp(LocalDateTime.now());

        // 2. Save to Database
        busRepository.save(bus);

        // 3. Return it (Spring automatically sends this to all subscribers)
        return bus;
    }
}