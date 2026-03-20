package com.example.ticket.model;

import jakarta.validation.constraints.NotBlank;

public record TrainQueryRequest(
        @NotBlank String date,
        @NotBlank String trainNo,
        @NotBlank String fromStation,
        @NotBlank String toStation) {
}
