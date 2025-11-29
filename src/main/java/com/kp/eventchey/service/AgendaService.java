package com.kp.eventchey.service;

import com.kp.eventchey.dto.request.AddAgendaItemRequest;
import com.kp.eventchey.dto.response.AgendaItemResponse;

import java.util.List;

public interface AgendaService {

    AgendaItemResponse addAgendaItem(String eventId, AddAgendaItemRequest request);

    List<AgendaItemResponse> getAgendaItems(String eventId);
}

