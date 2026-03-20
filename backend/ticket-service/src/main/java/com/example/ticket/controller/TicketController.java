package com.example.ticket.controller;

import com.example.ticket.model.TicketRequest;
import com.example.ticket.model.TicketResponse;
import com.example.ticket.model.TrainQueryRequest;
import com.example.ticket.service.TicketingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    private final TicketingService ticketingService;

    public TicketController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @PostMapping("/query")
    public Map<String, String> query(@Valid @RequestBody TrainQueryRequest request) {
        return ticketingService.querySeatAndStock(request.date(), request.trainNo(), request.fromStation(), request.toStation());
    }

    @PostMapping("/book")
    public TicketResponse book(@Valid @RequestBody TicketRequest request) {
        return ticketingService.occupy(request);
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
