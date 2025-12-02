package com.kp.eventchey.controller;

import com.kp.eventchey.dto.request.AddAgendaItemRequest;
import com.kp.eventchey.dto.response.AgendaItemResponse;
import com.kp.eventchey.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
             allowCredentials = "true",
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH})
@Tag(name = "Agenda Management", description = "APIs for managing event agenda items")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping("/{eventId}/agenda")
    @Operation(summary = "Add agenda item to an event")
    public ResponseEntity<AgendaItemResponse> addAgendaItem(
            @PathVariable String eventId,
            @Valid @RequestBody AddAgendaItemRequest request) {
        AgendaItemResponse response = agendaService.addAgendaItem(eventId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}/agenda")
    @Operation(summary = "Get all agenda items for an event")
    public ResponseEntity<List<AgendaItemResponse>> getAgendaItems(@PathVariable String eventId) {
        List<AgendaItemResponse> response = agendaService.getAgendaItems(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/agenda/{agendaId}/summary")
    @Operation(summary = "Summarize Agenda with AI")
    public ResponseEntity<String> summarizeAgenda(
            @PathVariable String eventId,
            @PathVariable String agendaId) {
        String summary = agendaService.generateAgendaSummary(eventId, agendaId);
        return ResponseEntity.ok(summary);
    }
}

