package com.kp.eventchey.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgendaItem {
    private String id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String speaker;
    private List<Question> questions;
    private List<Poll> polls;
    private String aiSummary;

    public AgendaItem() {
        this.questions = new ArrayList<>();
        this.polls = new ArrayList<>();
    }

    public AgendaItem(String id, String title, LocalDateTime startTime, LocalDateTime endTime,
                      String description, String speaker, List<Question> questions,
                      List<Poll> polls, String aiSummary) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.speaker = speaker;
        this.questions = questions != null ? questions : new ArrayList<>();
        this.polls = polls != null ? polls : new ArrayList<>();
        this.aiSummary = aiSummary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgendaItem that = (AgendaItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(description, that.description) &&
                Objects.equals(speaker, that.speaker) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(polls, that.polls) &&
                Objects.equals(aiSummary, that.aiSummary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, startTime, endTime, description, speaker,
                          questions, polls, aiSummary);
    }

    @Override
    public String toString() {
        return "AgendaItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                ", speaker='" + speaker + '\'' +
                ", questions=" + questions +
                ", polls=" + polls +
                ", aiSummary='" + aiSummary + '\'' +
                '}';
    }
}

