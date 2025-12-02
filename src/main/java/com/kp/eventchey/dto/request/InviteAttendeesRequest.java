package com.kp.eventchey.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InviteAttendeesRequest(
        @NotNull(message = "Attendees list is required")
        @Valid
        List<InviteAttendeeRequest> attendees
) {
}

