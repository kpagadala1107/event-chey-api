package com.kp.eventchey.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String createdBy;
    private List<Attendee> attendees;
    private List<AgendaItem> agenda;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String cachedAiSummary;
    private LocalDateTime aiSummaryGeneratedAt;

    public Event() {
        this.attendees = new ArrayList<>();
        this.agenda = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Event(String id, String name, String description, LocalDateTime startDate,
                 LocalDateTime endDate, String createdBy, List<Attendee> attendees,
                 List<AgendaItem> agenda, LocalDateTime createdAt, LocalDateTime updatedAt,
                 String cachedAiSummary, LocalDateTime aiSummaryGeneratedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.attendees = attendees != null ? attendees : new ArrayList<>();
        this.agenda = agenda != null ? agenda : new ArrayList<>();
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
        this.cachedAiSummary = cachedAiSummary;
        this.aiSummaryGeneratedAt = aiSummaryGeneratedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public List<AgendaItem> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<AgendaItem> agenda) {
        this.agenda = agenda;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCachedAiSummary() {
        return cachedAiSummary;
    }

    public void setCachedAiSummary(String cachedAiSummary) {
        this.cachedAiSummary = cachedAiSummary;
    }

    public LocalDateTime getAiSummaryGeneratedAt() {
        return aiSummaryGeneratedAt;
    }

    public void setAiSummaryGeneratedAt(LocalDateTime aiSummaryGeneratedAt) {
        this.aiSummaryGeneratedAt = aiSummaryGeneratedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(startDate, event.startDate) &&
                Objects.equals(endDate, event.endDate) &&
                Objects.equals(createdBy, event.createdBy) &&
                Objects.equals(attendees, event.attendees) &&
                Objects.equals(agenda, event.agenda) &&
                Objects.equals(createdAt, event.createdAt) &&
                Objects.equals(updatedAt, event.updatedAt) &&
                Objects.equals(cachedAiSummary, event.cachedAiSummary) &&
                Objects.equals(aiSummaryGeneratedAt, event.aiSummaryGeneratedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate, createdBy,
                          attendees, agenda, createdAt, updatedAt, cachedAiSummary, aiSummaryGeneratedAt);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdBy='" + createdBy + '\'' +
                ", attendees=" + attendees +
                ", agenda=" + agenda +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", cachedAiSummary='" + cachedAiSummary + '\'' +
                ", aiSummaryGeneratedAt=" + aiSummaryGeneratedAt +
                '}';
    }
}

