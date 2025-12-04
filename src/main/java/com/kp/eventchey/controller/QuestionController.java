package com.kp.eventchey.controller;

import com.kp.eventchey.dto.request.AddQuestionRequest;
import com.kp.eventchey.dto.request.AnswerQuestionRequest;
import com.kp.eventchey.dto.response.QuestionResponse;
import com.kp.eventchey.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
             allowCredentials = "true",
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH})
@Tag(name = "Question & Answer Management", description = "APIs for managing Q&A in agenda items")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/{eventId}/agenda/{agendaId}/questions")
    @Operation(summary = "Add a question to an agenda item")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable String eventId,
            @PathVariable String agendaId,
            @Valid @RequestBody AddQuestionRequest request) {
        QuestionResponse response = questionService.addQuestion(eventId, agendaId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{eventId}/agenda/{agendaId}/questions/{questionId}/answer")
    @Operation(summary = "Answer a question")
    public ResponseEntity<QuestionResponse> answerQuestion(
            @PathVariable String eventId,
            @PathVariable String agendaId,
            @PathVariable String questionId,
            @Valid @RequestBody AnswerQuestionRequest request) {
        QuestionResponse response = questionService.answerQuestion(eventId, agendaId, questionId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{eventId}/agenda/{agendaId}/questions/{questionId}/answer")
    @Operation(summary = "edit answer to a question")
    public ResponseEntity<QuestionResponse> updateAnswer(
            @PathVariable String eventId,
            @PathVariable String agendaId,
            @PathVariable String questionId,
            @Valid @RequestBody AnswerQuestionRequest request) {
        QuestionResponse response = questionService.answerQuestion(eventId, agendaId, questionId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/agenda/{agendaId}/questions")
    @Operation(summary = "List all questions for an agenda item")
    public ResponseEntity<List<QuestionResponse>> listQuestions(
            @PathVariable String eventId,
            @PathVariable String agendaId) {
        List<QuestionResponse> response = questionService.listQuestions(eventId, agendaId);
        return ResponseEntity.ok(response);
    }
}

