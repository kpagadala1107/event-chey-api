package com.kp.eventchey.mapper;

import com.kp.eventchey.domain.Attendee;
import com.kp.eventchey.dto.response.AttendeeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendeeMapper {

    AttendeeResponse toResponse(Attendee attendee);
}

