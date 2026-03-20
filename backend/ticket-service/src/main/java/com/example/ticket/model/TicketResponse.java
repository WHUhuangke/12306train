package com.example.ticket.model;

import java.util.List;

public record TicketResponse(String orderId, String status, List<String> seats, String message) {
}
