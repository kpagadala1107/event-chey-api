package com.kp.eventchey.service.impl;

import com.kp.eventchey.domain.AgendaItem;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.domain.Poll;
import com.kp.eventchey.dto.request.CreatePollRequest;
import com.kp.eventchey.dto.request.SubmitVoteRequest;
import com.kp.eventchey.dto.response.PollResponse;
import com.kp.eventchey.exception.BadRequestException;
import com.kp.eventchey.exception.ResourceNotFoundException;
import com.kp.eventchey.mapper.PollMapper;
import com.kp.eventchey.repository.EventRepository;
import com.kp.eventchey.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class PollServiceImpl implements PollService {

    private static final Logger logger = LoggerFactory.getLogger(PollServiceImpl.class);

    private final EventRepository eventRepository;
    private final PollMapper pollMapper;

    public PollServiceImpl(EventRepository eventRepository, PollMapper pollMapper) {
        this.eventRepository = eventRepository;
        this.pollMapper = pollMapper;
    }

    @Override
    public PollResponse createPoll(String agendaId, CreatePollRequest request) {
        logger.info("Creating poll for agenda: {}", agendaId);

        Event event = eventRepository.findEventByAgendaItemId(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        AgendaItem agendaItem = event.getAgenda().stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        Poll poll = new Poll();
        poll.setId(UUID.randomUUID().toString());
        poll.setQuestion(request.question());
        poll.setOptions(request.options());

        // Initialize votes for each option
        HashMap<String, Integer> votes = new HashMap<>();
        for (String option : request.options()) {
            votes.put(option, 0);
        }
        poll.setVotes(votes);

        if (agendaItem.getPolls() == null) {
            agendaItem.setPolls(new ArrayList<>());
        }

        agendaItem.getPolls().add(poll);
        event.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
        logger.info("Poll created: {}", poll.getId());

        return pollMapper.toResponse(poll);
    }

    @Override
    public PollResponse submitVote(String agendaId, String pollId, SubmitVoteRequest request) {
        logger.info("Submitting vote for poll {} in agenda {}", pollId, agendaId);

        Event event = eventRepository.findEventByAgendaItemId(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        AgendaItem agendaItem = event.getAgenda().stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        Poll poll = agendaItem.getPolls().stream()
                .filter(p -> p.getId().equals(pollId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

        // Validate option
        if (!poll.getOptions().contains(request.option())) {
            throw new BadRequestException("Invalid poll option: " + request.option());
        }

        // Increment vote count
        poll.getVotes().put(request.option(), poll.getVotes().get(request.option()) + 1);
        event.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
        logger.info("Vote submitted for option: {}", request.option());

        return pollMapper.toResponse(poll);
    }

    @Override
    public List<PollResponse> listPolls(String agendaId) {
        logger.info("Listing polls for agenda: {}", agendaId);

        Event event = eventRepository.findEventByAgendaItemId(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        AgendaItem agendaItem = event.getAgenda().stream()
                .filter(a -> a.getId().equals(agendaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("AgendaItem", "id", agendaId));

        List<Poll> polls = agendaItem.getPolls() != null ? agendaItem.getPolls() : new ArrayList<>();
        logger.info("Found {} polls", polls.size());

        return polls.stream()
                .map(pollMapper::toResponse)
                .toList();
    }
}

