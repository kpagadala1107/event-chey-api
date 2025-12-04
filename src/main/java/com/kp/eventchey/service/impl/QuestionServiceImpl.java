package com.kp.eventchey.service.impl;

import com.kp.eventchey.ai.AiSummaryService;
import com.kp.eventchey.domain.AgendaItem;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.domain.Question;
import com.kp.eventchey.dto.request.AddQuestionRequest;
import com.kp.eventchey.dto.request.AnswerQuestionRequest;
import com.kp.eventchey.dto.response.QuestionResponse;
import com.kp.eventchey.exception.ResourceNotFoundException;
import com.kp.eventchey.mapper.QuestionMapper;
import com.kp.eventchey.repository.EventRepository;
import com.kp.eventchey.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final EventRepository eventRepository;
    private final QuestionMapper questionMapper;
    private final AiSummaryService aiSummaryService;

    public QuestionServiceImpl(EventRepository eventRepository, QuestionMapper questionMapper,
                              AiSummaryService aiSummaryService) {
        this.eventRepository = eventRepository;
        this.questionMapper = questionMapper;
        this.aiSummaryService = aiSummaryService;
    }

    @Override
    public QuestionResponse addQuestion(String eventId, String agendaId, AddQuestionRequest request) {
        logger.info("Adding question to agenda: {} in event: {}", agendaId, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        AgendaItem agendaItem = event.getAgenda().stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        Question question = new Question();
        question.setId(UUID.randomUUID().toString());
        question.setAskedBy(request.askedBy());
        question.setQuestion(request.question());
        question.setTimestamp(LocalDateTime.now());
        question.setUpvotes(0);

        if (agendaItem.getQuestions() == null) {
            agendaItem.setQuestions(new ArrayList<>());
        }

        agendaItem.getQuestions().add(question);
        event.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
        logger.info("Question added: {}", question.getId());

        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse answerQuestion(String eventId, String agendaId, String questionId, AnswerQuestionRequest request) {
        logger.info("Answering question {} in agenda {} in event {}", questionId, agendaId, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        AgendaItem agendaItem = event.getAgenda().stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        Question question = agendaItem.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));

        question.setAnswer(request.answer());
        event.setUpdatedAt(LocalDateTime.now());

        // Update AI summary with new Q&A
        String aiSummary = aiSummaryService.summarizeQna(agendaItem.getQuestions());
        agendaItem.setAiSummary(aiSummary);

        eventRepository.save(event);
        logger.info("Question answered: {}", questionId);

        return questionMapper.toResponse(question);
    }

    @Override
    public List<QuestionResponse> listQuestions(String eventId, String agendaId) {
        logger.info("Listing questions for agenda: {} in event: {}", agendaId, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        AgendaItem agendaItem = event.getAgenda().stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        List<Question> questions = agendaItem.getQuestions() != null ? agendaItem.getQuestions() : new ArrayList<>();
        logger.info("Found {} questions", questions.size());

        return questions.stream()
                .map(questionMapper::toResponse)
                .toList();
    }
}

