package com.kp.eventchey.service;

import com.kp.eventchey.dto.request.AddAgendaItemRequest;
import com.kp.eventchey.dto.request.UpdateAgendaItemRequest;
import com.kp.eventchey.dto.response.AgendaItemResponse;

import java.util.List;

public interface AgendaService {

    AgendaItemResponse addAgendaItem(String eventId, AddAgendaItemRequest request);

    List<AgendaItemResponse> getAgendaItems(String eventId);

    String generateAgendaSummary(String eventId, String agendaId);

    AgendaItemResponse updateAgendaItem(String eventId, String agendaId, UpdateAgendaItemRequest request);
}

