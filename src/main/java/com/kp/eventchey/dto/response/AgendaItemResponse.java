package com.kp.eventchey.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AgendaItemResponse(
        String id,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String description,
        String speaker,
        List<QuestionResponse> questions,
        List<PollResponse> polls,
        String aiSummary
) {
}

