package com.kp.eventchey.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Question {
    private String id;
    private String attendeeId;
    private String questionText;
    private String answerText;
    private LocalDateTime timestamp;
    private Integer upvotes;

    public Question() {
        this.upvotes = 0;
    }

    public Question(String id, String attendeeId, String questionText, String answerText,
                    LocalDateTime timestamp, Integer upvotes) {
        this.id = id;
        this.attendeeId = attendeeId;
        this.questionText = questionText;
        this.answerText = answerText;
        this.timestamp = timestamp;
        this.upvotes = upvotes != null ? upvotes : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) &&
                Objects.equals(attendeeId, question.attendeeId) &&
                Objects.equals(questionText, question.questionText) &&
                Objects.equals(answerText, question.answerText) &&
                Objects.equals(timestamp, question.timestamp) &&
                Objects.equals(upvotes, question.upvotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attendeeId, questionText, answerText, timestamp, upvotes);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", attendeeId='" + attendeeId + '\'' +
                ", questionText='" + questionText + '\'' +
                ", answerText='" + answerText + '\'' +
                ", timestamp=" + timestamp +
                ", upvotes=" + upvotes +
                '}';
    }
}

