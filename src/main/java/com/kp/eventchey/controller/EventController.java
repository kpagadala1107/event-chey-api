package com.kp.eventchey.controller;

import com.kp.eventchey.ai.AiSummaryService;
import com.kp.eventchey.dto.request.CreateEventRequest;
import com.kp.eventchey.dto.request.InviteAttendeeRequest;
import com.kp.eventchey.dto.request.InviteAttendeesRequest;
import com.kp.eventchey.dto.request.UpdateAttendeeStatusRequest;
import com.kp.eventchey.dto.request.UpdateEventRequest;
import com.kp.eventchey.dto.response.AttendeeResponse;
import com.kp.eventchey.dto.response.EventResponse;
import com.kp.eventchey.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
             allowCredentials = "true",
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH})
@Tag(name = "Event Management", description = "APIs for managing events")
public class EventController {

    private final EventService eventService;
    private final AiSummaryService aiSummaryService;

    public EventController(EventService eventService, AiSummaryService aiSummaryService) {
        this.eventService = eventService;
        this.aiSummaryService = aiSummaryService;
    }

    @PostMapping
    @Operation(summary = "Create a new event")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventResponse> getEventById(@PathVariable String id) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable String id,
            @Valid @RequestBody UpdateEventRequest request) {
        EventResponse response = eventService.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/attendees")
    @Operation(summary = "Get all attendees for an event")
    public ResponseEntity<List<AttendeeResponse>> getAttendees(@PathVariable String eventId) {
        List<AttendeeResponse> response = eventService.getAttendees(eventId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{eventId}/attendees/invite")
    @Operation(summary = "Invite attendees to an event")
    public ResponseEntity<EventResponse> inviteAttendees(
            @PathVariable String eventId,
            @Valid @RequestBody InviteAttendeesRequest request) {
        EventResponse response = eventService.inviteAttendees(eventId, request.attendees());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{eventId}/attendees/{attendeeId}")
    @Operation(summary = "Remove an attendee from an event")
    public ResponseEntity<EventResponse> removeAttendee(
            @PathVariable String eventId,
            @PathVariable String attendeeId) {
        EventResponse response = eventService.removeAttendee(eventId, attendeeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{eventId}/attendees/{attendeeId}")
    @Operation(summary = "Update attendee status")
    public ResponseEntity<EventResponse> updateAttendeeStatus(
            @PathVariable String eventId,
            @PathVariable String attendeeId,
            @Valid @RequestBody UpdateAttendeeStatusRequest request) {
        EventResponse response = eventService.updateAttendeeStatus(eventId, attendeeId, request.status());
        return ResponseEntity.ok(response);
    }

    // Legacy endpoint for backward compatibility
    @PostMapping("/{id}/invite")
    @Operation(summary = "Invite attendees to an event (legacy)")
    @Deprecated
    public ResponseEntity<EventResponse> inviteAttendeesLegacy(
            @PathVariable String id,
            @Valid @RequestBody List<InviteAttendeeRequest> attendees) {
        EventResponse response = eventService.inviteAttendees(id, attendees);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List events with optional filters")
    public ResponseEntity<List<EventResponse>> listEvents(
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<EventResponse> response = eventService.listEvents(createdBy, from, to);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "Summarize Event with AI")
    public ResponseEntity<EventResponse> summarizeEvent(
            @PathVariable String id) {
        return ResponseEntity.ok(eventService.generateEventSummary(id));
    }

}

