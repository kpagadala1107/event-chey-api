package com.kp.eventchey.mapper;

import com.kp.eventchey.domain.Event;
import com.kp.eventchey.dto.request.CreateEventRequest;
import com.kp.eventchey.dto.response.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AttendeeMapper.class, AgendaItemMapper.class})
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attendees", ignore = true)
    @Mapping(target = "agenda", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toEntity(CreateEventRequest request);

    EventResponse toResponse(Event event);

    List<EventResponse> toResponseList(List<Event> events);
}

