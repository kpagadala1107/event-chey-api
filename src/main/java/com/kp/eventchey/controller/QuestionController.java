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
@RequestMapping("/api/agenda")
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

    @PostMapping("/{agendaId}/questions")
    @Operation(summary = "Add a question to an agenda item")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable String agendaId,
            @Valid @RequestBody AddQuestionRequest request) {
        QuestionResponse response = questionService.addQuestion(agendaId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{agendaId}/questions/{questionId}/answer")
    @Operation(summary = "Answer a question")
    public ResponseEntity<QuestionResponse> answerQuestion(
            @PathVariable String agendaId,
            @PathVariable String questionId,
            @Valid @RequestBody AnswerQuestionRequest request) {
        QuestionResponse response = questionService.answerQuestion(agendaId, questionId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{agendaId}/questions")
    @Operation(summary = "List all questions for an agenda item")
    public ResponseEntity<List<QuestionResponse>> listQuestions(@PathVariable String agendaId) {
        List<QuestionResponse> response = questionService.listQuestions(agendaId);
        return ResponseEntity.ok(response);
    }
}

