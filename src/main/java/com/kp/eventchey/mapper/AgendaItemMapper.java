package com.kp.eventchey.mapper;

import com.kp.eventchey.domain.AgendaItem;
import com.kp.eventchey.dto.response.AgendaItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, PollMapper.class})
public interface AgendaItemMapper {

    AgendaItemResponse toResponse(AgendaItem agendaItem);

    List<AgendaItemResponse> toResponseList(List<AgendaItem> agendaItems);
}

