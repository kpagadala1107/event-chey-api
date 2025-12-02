package com.kp.eventchey.ai.impl;

import com.kp.eventchey.ai.AiSummaryService;
import com.kp.eventchey.domain.AgendaItem;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.domain.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiSummaryService implements AiSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiSummaryService.class);

    @Override
    public String summarizeAgenda(AgendaItem agendaItem) {
        logger.info("Generating AI summary for agenda item: {}", agendaItem.getId());
        // Stub implementation - no real API calls
        return String.format(
                "AI Summary not available in development mode. Agenda: '%s' by %s (Duration: %d minutes)",
                agendaItem.getTitle(),
                agendaItem.getSpeaker() != null ? agendaItem.getSpeaker() : "Unknown",
                agendaItem.getStartTime() != null && agendaItem.getEndTime() != null
                        ? java.time.Duration.between(agendaItem.getStartTime(), agendaItem.getEndTime()).toMinutes()
                        : 0
        );
    }

    @Override
    public String summarizeQna(List<Question> questions) {
        logger.info("Generating AI summary for {} questions", questions != null ? questions.size() : 0);
        // Stub implementation - no real API calls
        if (questions == null || questions.isEmpty()) {
            return "AI Summary not available in development mode. No questions to summarize.";
        }

        long answeredQuestions = questions.stream()
                .filter(q -> q.getAnswer() != null && !q.getAnswer().isEmpty())
                .count();

        return String.format(
                "AI Summary not available in development mode. Total Questions: %d, Answered: %d, Pending: %d",
                questions.size(),
                answeredQuestions,
                questions.size() - answeredQuestions
        );
    }

    @Override
    public String summarizeEvent(Event event) {
        logger.info("Generating AI summary for event: {}", event.getId());

        // Stub implementation - no real API calls
        return String.format(
                "AI Summary not available in development mode. Event: '%s' with %d attendees and %d agenda items. Created by: %s",
                event.getName(),
                event.getAttendees() != null ? event.getAttendees().size() : 0,
                event.getAgenda() != null ? event.getAgenda().size() : 0,
                event.getCreatedBy()
        );
    }
}

