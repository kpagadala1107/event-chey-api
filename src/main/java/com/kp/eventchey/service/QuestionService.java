package com.kp.eventchey.service;

import com.kp.eventchey.dto.request.AddQuestionRequest;
import com.kp.eventchey.dto.request.AnswerQuestionRequest;
import com.kp.eventchey.dto.response.QuestionResponse;

import java.util.List;

public interface QuestionService {

    QuestionResponse addQuestion(String agendaId, AddQuestionRequest request);

    QuestionResponse answerQuestion(String agendaId, String questionId, AnswerQuestionRequest request);

    List<QuestionResponse> listQuestions(String agendaId);
}

