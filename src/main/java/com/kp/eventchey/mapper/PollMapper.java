package com.kp.eventchey.mapper;

import com.kp.eventchey.domain.Poll;
import com.kp.eventchey.dto.response.PollResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PollMapper {

    PollResponse toResponse(Poll poll);
}

