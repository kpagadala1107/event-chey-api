package com.kp.eventchey.ai;

import com.kp.eventchey.domain.AgendaItem;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.domain.Question;

import java.util.List;

public interface AiSummaryService {

    String summarizeAgenda(AgendaItem agendaItem);

    String summarizeQna(List<Question> questions);

    String summarizeEvent(Event event);
}

