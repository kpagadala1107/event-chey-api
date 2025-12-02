package com.kp.eventchey.service;

import com.kp.eventchey.domain.Attendee;
import com.kp.eventchey.domain.Event;

import java.util.List;

public interface EmailService {

    /**
     * Send invitation email to a single attendee
     * @param event The event details
     * @param attendee The attendee to invite
     */
    void sendInvitationEmail(Event event, Attendee attendee);

    /**
     * Send invitation emails to multiple attendees
     * @param event The event details
     * @param attendees List of attendees to invite
     */
    void sendInvitationEmails(Event event, List<Attendee> attendees);

    /**
     * Send event update notification to all attendees
     * @param event The updated event
     */
    void sendEventUpdateNotification(Event event);

    /**
     * Send event cancellation notification to all attendees
     * @param event The cancelled event
     */
    void sendEventCancellationNotification(Event event);
}

