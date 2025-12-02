package com.kp.eventchey.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Question {
    private String id;
    private String askedBy;
    private String question;
    private String answer;
    private LocalDateTime timestamp;
    private Integer upvotes;

    public Question() {
        this.upvotes = 0;
    }

    public Question(String id, String askedBy, String question, String answer,
                    LocalDateTime timestamp, Integer upvotes) {
        this.id = id;
        this.askedBy = askedBy;
        this.question = question;
        this.answer = answer;
        this.timestamp = timestamp;
        this.upvotes = upvotes != null ? upvotes : 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(String askedBy) {
        this.askedBy = askedBy;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
                Objects.equals(askedBy, question.askedBy) &&
                Objects.equals(question, question.question) &&
                Objects.equals(answer, question.answer) &&
                Objects.equals(timestamp, question.timestamp) &&
                Objects.equals(upvotes, question.upvotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, askedBy, question, answer, timestamp, upvotes);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", askedBy='" + askedBy + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", timestamp=" + timestamp +
                ", upvotes=" + upvotes +
                '}';
    }
}

