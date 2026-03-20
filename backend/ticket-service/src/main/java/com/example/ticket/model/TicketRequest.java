package com.example.ticket.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TicketRequest(
        @NotBlank String userId,
        @NotBlank String trainNo,
        @NotBlank String travelDate,
        @NotBlank String fromStation,
        @NotBlank String toStation,
        @Min(1) @Max(3) int seatLevel,
        @NotEmpty List<String> preferredSeats,
        @Min(1) @Max(5) int passengerCount) {
}
