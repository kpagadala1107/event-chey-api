package com.kp.eventchey.repository;

import com.kp.eventchey.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findByCreatedBy(String createdBy);

    @Query("{ 'attendees.email': ?0 }")
    List<Event> findByAttendeesEmail(String email);

    @Query("{ 'agenda.id': ?0 }")
    Optional<Event> findEventByAgendaItemId(String agendaItemId);

    List<Event> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Event> findByCreatedByAndStartDateBetween(String createdBy, LocalDateTime startDate, LocalDateTime endDate);
}

