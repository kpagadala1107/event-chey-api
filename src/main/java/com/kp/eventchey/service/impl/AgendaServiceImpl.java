package com.kp.eventchey.service.impl;

import com.kp.eventchey.ai.AiSummaryService;
import com.kp.eventchey.domain.AgendaItem;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.dto.request.AddAgendaItemRequest;
import com.kp.eventchey.dto.request.UpdateAgendaItemRequest;
import com.kp.eventchey.dto.response.AgendaItemResponse;
import com.kp.eventchey.exception.ResourceNotFoundException;
import com.kp.eventchey.exception.ValidationException;
import com.kp.eventchey.mapper.AgendaItemMapper;
import com.kp.eventchey.repository.EventRepository;
import com.kp.eventchey.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AgendaServiceImpl implements AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final EventRepository eventRepository;
    private final AgendaItemMapper agendaItemMapper;
    private final AiSummaryService aiSummaryService;

    public AgendaServiceImpl(EventRepository eventRepository, AgendaItemMapper agendaItemMapper,
                            AiSummaryService aiSummaryService) {
        this.eventRepository = eventRepository;
        this.agendaItemMapper = agendaItemMapper;
        this.aiSummaryService = aiSummaryService;
    }

    @Override
    public AgendaItemResponse addAgendaItem(String eventId, AddAgendaItemRequest request) {
        logger.info("Adding agenda item to event: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        // Validate times
        if (request.endTime().isBefore(request.startTime())) {
            throw new ValidationException("End time must be after start time");
        }

        AgendaItem agendaItem = new AgendaItem();
        agendaItem.setId(UUID.randomUUID().toString());
        agendaItem.setTitle(request.title());
        agendaItem.setStartTime(request.startTime());
        agendaItem.setEndTime(request.endTime());
        agendaItem.setDescription(request.description());
        agendaItem.setSpeaker(request.speaker());
        agendaItem.setQuestions(new ArrayList<>());
        agendaItem.setPolls(new ArrayList<>());

        // Generate AI summary
        String aiSummary = aiSummaryService.summarizeAgenda(agendaItem);
        agendaItem.setAiSummary(aiSummary);

        if (event.getAgenda() == null) {
            event.setAgenda(new ArrayList<>());
        }

        event.getAgenda().add(agendaItem);
        event.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
        logger.info("Agenda item added: {}", agendaItem.getId());

        return agendaItemMapper.toResponse(agendaItem);
    }

    @Override
    public List<AgendaItemResponse> getAgendaItems(String eventId) {
        logger.info("Fetching agenda items for event: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        List<AgendaItem> agendaItems = event.getAgenda() != null ? event.getAgenda() : new ArrayList<>();
        logger.info("Found {} agenda items", agendaItems.size());

        return agendaItemMapper.toResponseList(agendaItems);
    }

    @Override
    public String generateAgendaSummary(String eventId, String agendaId) {
        logger.info("Generating AI summary for agenda: {}", agendaId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        List<AgendaItem> agendaItems = event.getAgenda() != null ? event.getAgenda() : new ArrayList<>();
        AgendaItem eventAgendaItem = agendaItems.stream()
                .filter(item -> item.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        return aiSummaryService.summarizeAgenda(eventAgendaItem);
    }

    @Override
    public AgendaItemResponse updateAgendaItem(String eventId, String agendaId, UpdateAgendaItemRequest request) {
        logger.info("Updating agenda item: {} for event: {}", agendaId, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        // Validate times
        if (request.endTime().isBefore(request.startTime())) {
            throw new ValidationException("End time must be after start time");
        }

        List<AgendaItem> agendaItems = event.getAgenda() != null ? event.getAgenda() : new ArrayList<>();
        AgendaItem agendaItem = agendaItems.stream()
                .filter(item -> item.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        // Update agenda item fields
        agendaItem.setTitle(request.title());
        agendaItem.setStartTime(request.startTime());
        agendaItem.setEndTime(request.endTime());
        agendaItem.setDescription(request.description());
        agendaItem.setSpeaker(request.speaker());

        // Regenerate AI summary with updated data
        String aiSummary = aiSummaryService.summarizeAgenda(agendaItem);
        agendaItem.setAiSummary(aiSummary);

        event.setUpdatedAt(LocalDateTime.now());
        eventRepository.save(event);

        logger.info("Agenda item updated: {}", agendaId);
        return agendaItemMapper.toResponse(agendaItem);
    }
}

