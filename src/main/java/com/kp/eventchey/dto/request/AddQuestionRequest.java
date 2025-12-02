package com.kp.eventchey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddQuestionRequest(
        @NotBlank(message = "Attendee ID is required")
        String askedBy,

        @NotBlank(message = "Question text is required")
        String question
) {
}

