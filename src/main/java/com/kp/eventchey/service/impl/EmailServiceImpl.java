package com.kp.eventchey.service.impl;

import com.kp.eventchey.domain.Attendee;
import com.kp.eventchey.domain.Event;
import com.kp.eventchey.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final boolean emailEnabled;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${app.email.from}") String fromEmail,
            @Value("${app.email.enabled}") boolean emailEnabled) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.emailEnabled = emailEnabled;
    }

    @Override
    public void sendInvitationEmail(Event event, Attendee attendee) {
        if (!emailEnabled) {
            logger.info("Email sending is disabled. Skipping invitation email to: {}", attendee.getEmail());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(attendee.getEmail());
            helper.setSubject("You're Invited: " + event.getName());
            helper.setText(buildInvitationEmailBody(event, attendee), true);

            mailSender.send(message);
            logger.info("Invitation email sent successfully to: {}", attendee.getEmail());
        } catch (MessagingException | MailException e) {
            logger.error("Failed to send invitation email to: {}", attendee.getEmail(), e);
            // Don't throw exception - we don't want email failures to break the invitation process
        }
    }

    @Override
    public void sendInvitationEmails(Event event, List<Attendee> attendees) {
        if (!emailEnabled) {
            logger.info("Email sending is disabled. Skipping invitation emails for {} attendees", attendees.size());
            return;
        }

        logger.info("Sending invitation emails to {} attendees", attendees.size());
        for (Attendee attendee : attendees) {
            sendInvitationEmail(event, attendee);
        }
    }

    @Override
    public void sendEventUpdateNotification(Event event) {
        if (!emailEnabled) {
            logger.info("Email sending is disabled. Skipping event update notification");
            return;
        }

        if (event.getAttendees() == null || event.getAttendees().isEmpty()) {
            logger.info("No attendees to notify for event: {}", event.getId());
            return;
        }

        logger.info("Sending event update notification to {} attendees", event.getAttendees().size());
        for (Attendee attendee : event.getAttendees()) {
            sendUpdateEmail(event, attendee);
        }
    }

    @Override
    public void sendEventCancellationNotification(Event event) {
        if (!emailEnabled) {
            logger.info("Email sending is disabled. Skipping event cancellation notification");
            return;
        }

        if (event.getAttendees() == null || event.getAttendees().isEmpty()) {
            logger.info("No attendees to notify for event: {}", event.getId());
            return;
        }

        logger.info("Sending cancellation notification to {} attendees", event.getAttendees().size());
        for (Attendee attendee : event.getAttendees()) {
            sendCancellationEmail(event, attendee);
        }
    }

    private void sendUpdateEmail(Event event, Attendee attendee) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(attendee.getEmail());
            helper.setSubject("Event Updated: " + event.getName());
            helper.setText(buildUpdateEmailBody(event, attendee), true);

            mailSender.send(message);
            logger.info("Update notification sent successfully to: {}", attendee.getEmail());
        } catch (MessagingException | MailException e) {
            logger.error("Failed to send update notification to: {}", attendee.getEmail(), e);
        }
    }

    private void sendCancellationEmail(Event event, Attendee attendee) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(attendee.getEmail());
            helper.setSubject("Event Cancelled: " + event.getName());
            helper.setText(buildCancellationEmailBody(event, attendee), true);

            mailSender.send(message);
            logger.info("Cancellation notification sent successfully to: {}", attendee.getEmail());
        } catch (MessagingException | MailException e) {
            logger.error("Failed to send cancellation notification to: {}", attendee.getEmail(), e);
        }
    }

    private String buildInvitationEmailBody(Event event, Attendee attendee) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                        .event-details { background-color: white; padding: 20px; margin: 20px 0; border-left: 4px solid #4CAF50; }
                        .detail-row { margin: 10px 0; }
                        .label { font-weight: bold; color: #555; }
                        .footer { text-align: center; margin-top: 30px; color: #777; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>You're Invited! 🎉</h1>
                        </div>
                        <div class="content">
                            <p>Hi %s,</p>
                            <p>You've been invited to attend the following event:</p>
                            
                            <div class="event-details">
                                <h2 style="margin-top: 0; color: #4CAF50;">%s</h2>
                                <div class="detail-row">
                                    <span class="label">📅 Starts:</span> %s
                                </div>
                                <div class="detail-row">
                                    <span class="label">🏁 Ends:</span> %s
                                </div>
                                %s
                                <div class="detail-row">
                                    <span class="label">👤 Organized by:</span> %s
                                </div>
                            </div>
                            
                            <p>We look forward to seeing you there!</p>
                            <p>Best regards,<br>Event Chey Team</p>
                        </div>
                        <div class="footer">
                            <p>This is an automated message from Event Chey. Please do not reply to this email.</p>
                        </div>
                    </div>
                </body>
                </html>
                """,
                attendee.getName(),
                event.getName(),
                event.getStartDate().format(DATE_FORMATTER),
                event.getEndDate().format(DATE_FORMATTER),
                event.getDescription() != null ? String.format("<div class=\"detail-row\"><span class=\"label\">📝 Description:</span> %s</div>", event.getDescription()) : "",
                event.getCreatedBy()
        );
    }

    private String buildUpdateEmailBody(Event event, Attendee attendee) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #FF9800; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                        .event-details { background-color: white; padding: 20px; margin: 20px 0; border-left: 4px solid #FF9800; }
                        .detail-row { margin: 10px 0; }
                        .label { font-weight: bold; color: #555; }
                        .footer { text-align: center; margin-top: 30px; color: #777; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Event Updated ℹ️</h1>
                        </div>
                        <div class="content">
                            <p>Hi %s,</p>
                            <p>The event details have been updated:</p>
                            
                            <div class="event-details">
                                <h2 style="margin-top: 0; color: #FF9800;">%s</h2>
                                <div class="detail-row">
                                    <span class="label">📅 Starts:</span> %s
                                </div>
                                <div class="detail-row">
                                    <span class="label">🏁 Ends:</span> %s
                                </div>
                                %s
                            </div>
                            
                            <p>Please make note of these changes.</p>
                            <p>Best regards,<br>Event Chey Team</p>
                        </div>
                        <div class="footer">
                            <p>This is an automated message from Event Chey. Please do not reply to this email.</p>
                        </div>
                    </div>
                </body>
                </html>
                """,
                attendee.getName(),
                event.getName(),
                event.getStartDate().format(DATE_FORMATTER),
                event.getEndDate().format(DATE_FORMATTER),
                event.getDescription() != null ? String.format("<div class=\"detail-row\"><span class=\"label\">📝 Description:</span> %s</div>", event.getDescription()) : ""
        );
    }

    private String buildCancellationEmailBody(Event event, Attendee attendee) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #f44336; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                        .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                        .event-details { background-color: white; padding: 20px; margin: 20px 0; border-left: 4px solid #f44336; }
                        .detail-row { margin: 10px 0; }
                        .label { font-weight: bold; color: #555; }
                        .footer { text-align: center; margin-top: 30px; color: #777; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Event Cancelled ❌</h1>
                        </div>
                        <div class="content">
                            <p>Hi %s,</p>
                            <p>We regret to inform you that the following event has been cancelled:</p>
                            
                            <div class="event-details">
                                <h2 style="margin-top: 0; color: #f44336;">%s</h2>
                                <div class="detail-row">
                                    <span class="label">📅 Was scheduled:</span> %s to %s
                                </div>
                            </div>
                            
                            <p>We apologize for any inconvenience this may cause.</p>
                            <p>Best regards,<br>Event Chey Team</p>
                        </div>
                        <div class="footer">
                            <p>This is an automated message from Event Chey. Please do not reply to this email.</p>
                        </div>
                    </div>
                </body>
                </html>
                """,
                attendee.getName(),
                event.getName(),
                event.getStartDate().format(DATE_FORMATTER),
                event.getEndDate().format(DATE_FORMATTER)
        );
    }
}

