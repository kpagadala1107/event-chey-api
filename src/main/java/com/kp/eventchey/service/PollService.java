package com.kp.eventchey.service;

import com.kp.eventchey.dto.request.CreatePollRequest;
import com.kp.eventchey.dto.request.SubmitVoteRequest;
import com.kp.eventchey.dto.response.PollResponse;

import java.util.List;

public interface PollService {

    PollResponse createPoll(String agendaId, CreatePollRequest request);

    PollResponse submitVote(String agendaId, String pollId, SubmitVoteRequest request);

    List<PollResponse> listPolls(String agendaId);
}

