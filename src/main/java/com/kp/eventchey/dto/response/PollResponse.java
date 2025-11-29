package com.kp.eventchey.dto.response;

import java.util.List;
import java.util.Map;

public record PollResponse(
        String id,
        String question,
        List<String> options,
        Map<String, Integer> votes
) {
}

