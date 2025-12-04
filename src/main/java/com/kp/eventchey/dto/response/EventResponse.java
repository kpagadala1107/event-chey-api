package com.kp.eventchey.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
        String id,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String createdBy,
        List<AttendeeResponse> attendees,
        List<AgendaItemResponse> agenda,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String cachedAiSummary,
        LocalDateTime aiSummaryGeneratedAt
) {
}

