package com.kp.eventchey.dto.response;

import java.time.LocalDateTime;

public record QuestionResponse(
        String id,
        String attendeeId,
        String questionText,
        String answerText,
        LocalDateTime timestamp,
        Integer upvotes
) {
}

