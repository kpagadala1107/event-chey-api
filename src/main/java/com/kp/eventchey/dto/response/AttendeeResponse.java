package com.kp.eventchey.dto.response;

import com.kp.eventchey.domain.AttendeeStatus;

public record AttendeeResponse(
        String id,
        String email,
        String phone,
        String name,
        AttendeeStatus status
) {
}

