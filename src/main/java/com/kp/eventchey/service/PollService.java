package com.kp.eventchey.service;

import com.kp.eventchey.dto.request.CreatePollRequest;
import com.kp.eventchey.dto.request.SubmitVoteRequest;
import com.kp.eventchey.dto.response.PollResponse;

import java.util.List;

public interface PollService {

    PollResponse createPoll(String eventId, String agendaId, CreatePollRequest request);

    PollResponse submitVote(String eventId, String agendaId, String pollId, SubmitVoteRequest request);

    List<PollResponse> listPolls(String eventId, String agendaId);
}

