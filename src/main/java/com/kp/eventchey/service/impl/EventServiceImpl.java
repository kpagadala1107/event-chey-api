package com.kp.eventchey.service.impl;

import com.kp.eventchey.ai.AiSummaryService;
import com.kp.eventchey.domain.Attendee;
import com.kp.eventchey.domain.AttendeeStatus;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.dto.request.CreateEventRequest;
import com.kp.eventchey.dto.request.InviteAttendeeRequest;
import com.kp.eventchey.dto.request.UpdateEventRequest;
import com.kp.eventchey.dto.response.EventResponse;
import com.kp.eventchey.exception.ResourceNotFoundException;
import com.kp.eventchey.exception.ValidationException;
import com.kp.eventchey.mapper.EventMapper;
import com.kp.eventchey.repository.EventRepository;
import com.kp.eventchey.service.EmailService;
import com.kp.eventchey.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AiSummaryService aiSummaryService;
    private final EmailService emailService;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper,
                           AiSummaryService aiSummaryService, EmailService emailService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.aiSummaryService = aiSummaryService;
        this.emailService = emailService;
    }

    @Override
    public EventResponse createEvent(CreateEventRequest request) {
        logger.info("Creating event: {}", request.name());

        // Validate dates
        if (request.endDate().isBefore(request.startDate())) {
            throw new ValidationException("End date must be after start date");
        }

        Event event = eventMapper.toEntity(request);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        Event savedEvent = eventRepository.save(event);
        logger.info("Event created with ID: {}", savedEvent.getId());

        return eventMapper.toResponse(savedEvent);
    }

    @Override
    public EventResponse updateEvent(String eventId, UpdateEventRequest request) {
        logger.info("Updating event: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (request.name() != null) {
            event.setName(request.name());
        }
        if (request.description() != null) {
            event.setDescription(request.description());
        }
        if (request.startDate() != null) {
            event.setStartDate(request.startDate());
        }
        if (request.endDate() != null) {
            event.setEndDate(request.endDate());
        }

        // Validate dates
        if (event.getEndDate().isBefore(event.getStartDate())) {
            throw new ValidationException("End date must be after start date");
        }

        event.setUpdatedAt(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(event);

        logger.info("Event updated: {}", eventId);
        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    public EventResponse getEventById(String eventId) {
        logger.info("Fetching event: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse inviteAttendees(String eventId, List<InviteAttendeeRequest> attendeeRequests) {
        logger.info("Inviting {} attendees to event: {}", attendeeRequests.size(), eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getAttendees() == null) {
            event.setAttendees(new ArrayList<>());
        }

        List<Attendee> newAttendees = new ArrayList<>();

        for (InviteAttendeeRequest request : attendeeRequests) {
            // Check if attendee already exists
            boolean exists = event.getAttendees().stream()
                    .anyMatch(a -> a.getEmail().equalsIgnoreCase(request.email()));

            if (!exists) {
                Attendee attendee = new Attendee();
                attendee.setId(UUID.randomUUID().toString());
                attendee.setEmail(request.email());
                attendee.setPhone(request.phone());
                attendee.setName(request.name());
                attendee.setStatus(AttendeeStatus.INVITED);

                event.getAttendees().add(attendee);
                newAttendees.add(attendee);
                logger.info("Added attendee: {}", request.email());
            } else {
                logger.warn("Attendee already invited: {}", request.email());
            }
        }

        event.setUpdatedAt(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(event);

        // Send invitation emails to new attendees
        if (!newAttendees.isEmpty()) {
            logger.info("Sending invitation emails to {} new attendees", newAttendees.size());
            emailService.sendInvitationEmails(updatedEvent, newAttendees);
        }

        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    public List<EventResponse> listEvents(String createdBy, LocalDateTime from, LocalDateTime to) {
        logger.info("Listing events with filters - createdBy: {}, from: {}, to: {}", createdBy, from, to);

        List<Event> events;

        if (createdBy != null && from != null && to != null) {
            events = eventRepository.findByCreatedByAndStartDateBetween(createdBy, from, to);
        } else if (createdBy != null) {
            events = eventRepository.findByCreatedBy(createdBy);
        } else if (from != null && to != null) {
            events = eventRepository.findByStartDateBetween(from, to);
        } else {
            events = eventRepository.findAll();
        }

        logger.info("Found {} events", events.size());
        return eventMapper.toResponseList(events);
    }

    @Override
    public String generateEventSummary(String eventId) {
        logger.info("Generating AI summary for event: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        return aiSummaryService.summarizeEvent(event);
    }

    @Override
    public List<com.kp.eventchey.dto.response.AttendeeResponse> getAttendees(String eventId) {
        logger.info("Getting attendees for event: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getAttendees() == null || event.getAttendees().isEmpty()) {
            return new ArrayList<>();
        }

        return event.getAttendees().stream()
                .map(attendee -> new com.kp.eventchey.dto.response.AttendeeResponse(
                        attendee.getId(),
                        attendee.getEmail(),
                        attendee.getPhone(),
                        attendee.getName(),
                        attendee.getStatus()
                ))
                .toList();
    }

    @Override
    public EventResponse removeAttendee(String eventId, String attendeeId) {
        logger.info("Removing attendee {} from event: {}", attendeeId, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getAttendees() == null) {
            throw new ResourceNotFoundException("Attendee", "id", attendeeId);
        }

        boolean removed = event.getAttendees().removeIf(a -> a.getId().equals(attendeeId));

        if (!removed) {
            throw new ResourceNotFoundException("Attendee", "id", attendeeId);
        }

        event.setUpdatedAt(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(event);

        logger.info("Attendee {} removed from event: {}", attendeeId, eventId);
        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    public EventResponse updateAttendeeStatus(String eventId, String attendeeId, AttendeeStatus status) {
        logger.info("Updating attendee {} status to {} for event: {}", attendeeId, status, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getAttendees() == null) {
            throw new ResourceNotFoundException("Attendee", "id", attendeeId);
        }

        Attendee attendee = event.getAttendees().stream()
                .filter(a -> a.getId().equals(attendeeId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Attendee", "id", attendeeId));

        attendee.setStatus(status);
        event.setUpdatedAt(LocalDateTime.now());
        Event updatedEvent = eventRepository.save(event);

        logger.info("Attendee {} status updated to {} for event: {}", attendeeId, status, eventId);
        return eventMapper.toResponse(updatedEvent);
    }
}

