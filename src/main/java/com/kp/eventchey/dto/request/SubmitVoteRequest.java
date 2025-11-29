package com.kp.eventchey.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubmitVoteRequest(
        @NotBlank(message = "Option is required")
        String option
) {
}

