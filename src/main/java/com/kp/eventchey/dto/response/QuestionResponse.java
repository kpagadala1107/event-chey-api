package com.kp.eventchey.dto.response;

import java.time.LocalDateTime;

public record QuestionResponse(
        String id,
        String askedBy,
        String question,
        String answer,
        LocalDateTime timestamp,
        Integer upvotes
) {
}

