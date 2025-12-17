package com.nibm.lankatransit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JwtUtils jwtUtils;

    // 1. Buy a Ticket
    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody TicketRequest request) {
        // Get the currently logged-in user from the Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Ticket ticket = new Ticket();
        ticket.setPassengerUsername(currentUsername);
        ticket.setBusId(request.getBusId());
        ticket.setAmount(100.00); // Fixed price for now, or fetch from DB
        ticket.setPurchaseTime(LocalDateTime.now());

        // GENERATE QR DATA: We sign this so it can't be faked
        // Format: TICKET:ID:USER:BUS:TIME
        String rawData = "TICKET:" + currentUsername + ":" + request.getBusId() + ":" + System.currentTimeMillis();
        // We use the same JWT logic to sign it, acting as a digital signature
        String digitalSignature = jwtUtils.generateToken(rawData);

        ticket.setQrData(digitalSignature);

        return ticketRepository.save(ticket);
    }

    // 2. View My Tickets
    @GetMapping("/my-tickets")
    public List<Ticket> getMyTickets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ticketRepository.findByPassengerUsername(auth.getName());
    }
}

// Helper class for the JSON body
class TicketRequest {
    private String busId;
    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }
}