package com.kp.eventchey.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AnswerQuestionRequest(
        @NotBlank(message = "Answer text is required")
        String answer
) {
}

