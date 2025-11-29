package com.kp.eventchey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePollRequest(
        @NotBlank(message = "Poll question is required")
        String question,

        @NotEmpty(message = "At least one option is required")
        @Size(min = 2, message = "At least two options are required")
        List<String> options
) {
}

