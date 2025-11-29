package com.kp.eventchey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AddAgendaItemRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotNull(message = "Start time is required")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        LocalDateTime endTime,

        String description,

        String speaker
) {
}

