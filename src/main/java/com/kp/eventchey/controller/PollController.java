package com.kp.eventchey.controller;

import com.kp.eventchey.dto.request.CreatePollRequest;
import com.kp.eventchey.dto.request.SubmitVoteRequest;
import com.kp.eventchey.dto.response.PollResponse;
import com.kp.eventchey.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
             allowCredentials = "true",
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH})
@Tag(name = "Poll Management", description = "APIs for managing live polls and voting")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping("/{agendaId}/polls")
    @Operation(summary = "Create a poll for an agenda item")
    public ResponseEntity<PollResponse> createPoll(
            @PathVariable String agendaId,
            @Valid @RequestBody CreatePollRequest request) {
        PollResponse response = pollService.createPoll(agendaId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{agendaId}/polls/{pollId}/vote")
    @Operation(summary = "Submit a vote for a poll")
    public ResponseEntity<PollResponse> submitVote(
            @PathVariable String agendaId,
            @PathVariable String pollId,
            @Valid @RequestBody SubmitVoteRequest request) {
        PollResponse response = pollService.submitVote(agendaId, pollId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{agendaId}/polls")
    @Operation(summary = "List all polls for an agenda item")
    public ResponseEntity<List<PollResponse>> listPolls(@PathVariable String agendaId) {
        List<PollResponse> response = pollService.listPolls(agendaId);
        return ResponseEntity.ok(response);
    }
}

