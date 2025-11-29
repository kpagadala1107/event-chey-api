package com.kp.eventchey.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateEventRequest(
        @NotBlank(message = "Event name is required")
        String name,

        @NotBlank(message = "Event description is required")
        String description,

        @NotNull(message = "Start date is required")
        @Future(message = "Start date must be in the future")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        @Future(message = "End date must be in the future")
        LocalDateTime endDate,

        @NotBlank(message = "Created by is required")
        String createdBy
) {
}

