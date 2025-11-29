package com.kp.eventchey.dto.request;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

