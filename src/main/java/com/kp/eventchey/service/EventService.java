package com.kp.eventchey.service;

import com.kp.eventchey.domain.AttendeeStatus;
import com.kp.eventchey.dto.request.CreateEventRequest;
import com.kp.eventchey.dto.request.InviteAttendeeRequest;
import com.kp.eventchey.dto.request.UpdateEventRequest;
import com.kp.eventchey.dto.response.AttendeeResponse;
import com.kp.eventchey.dto.response.EventResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventResponse createEvent(CreateEventRequest request);

    EventResponse updateEvent(String eventId, UpdateEventRequest request);

    EventResponse getEventById(String eventId);

    EventResponse inviteAttendees(String eventId, List<InviteAttendeeRequest> attendees);

    List<EventResponse> listEvents(String createdBy, LocalDateTime from, LocalDateTime to);

    EventResponse generateEventSummary(String eventId);

    List<AttendeeResponse> getAttendees(String eventId);

    EventResponse removeAttendee(String eventId, String attendeeId);

    EventResponse updateAttendeeStatus(String eventId, String attendeeId, AttendeeStatus status);

    EventResponse deleteEvent(String eventId);
}

