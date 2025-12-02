package com.kp.eventchey.dto.request;

import com.kp.eventchey.domain.AttendeeStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAttendeeStatusRequest(
        @NotNull(message = "Status is required")
        AttendeeStatus status
) {
}

