# Email Invitation Feature

## Overview

The Event Chey API now supports sending email invitations to attendees when they are invited to events. This feature uses Spring Boot's email capabilities to send beautifully formatted HTML emails.

## Features

- **Invitation Emails**: Automatically sent when attendees are invited to an event
- **Event Update Notifications**: Notify attendees when event details change
- **Event Cancellation Notifications**: Notify attendees when an event is cancelled
- **HTML Email Templates**: Professional, responsive email templates with event details
- **Configurable**: Email sending can be enabled/disabled via configuration

## Configuration

### Email Settings

Add the following configuration to your `application.properties` or set them as environment variables:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME:your-email@gmail.com}
spring.mail.password=${EMAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# Application Email Settings
app.email.from=${EMAIL_FROM:noreply@eventchey.com}
app.email.enabled=${EMAIL_ENABLED:false}
```

### Environment Variables

Set these environment variables to configure email:

- `EMAIL_USERNAME`: Your email account username (e.g., your Gmail address)
- `EMAIL_PASSWORD`: Your email account password or app-specific password
- `EMAIL_FROM`: The email address to send from
- `EMAIL_ENABLED`: Set to `true` to enable email sending, `false` to disable

### Gmail Configuration

If using Gmail, you need to:

1. Enable 2-factor authentication on your Google account
2. Generate an [App Password](https://myaccount.google.com/apppasswords)
3. Use the app password as `EMAIL_PASSWORD`

### Other Email Providers

For other email providers, update these properties:

```properties
spring.mail.host=smtp.your-provider.com
spring.mail.port=587
```

Common SMTP settings:
- **Gmail**: smtp.gmail.com:587
- **Outlook**: smtp-mail.outlook.com:587
- **Yahoo**: smtp.mail.yahoo.com:587
- **SendGrid**: smtp.sendgrid.net:587
- **AWS SES**: email-smtp.us-east-1.amazonaws.com:587

## Usage

### Inviting Attendees

When you invite attendees via the API, emails are automatically sent:

```bash
POST /api/events/{eventId}/invite
Content-Type: application/json

[
  {
    "email": "john@example.com",
    "name": "John Doe",
    "phone": "+1234567890"
  },
  {
    "email": "jane@example.com",
    "name": "Jane Smith",
    "phone": "+1234567891"
  }
]
```

Each attendee will receive an email with:
- Event name and description
- Start and end date/time
- Organizer information
- Professional HTML formatting

### Email Templates

The service includes three types of email templates:

1. **Invitation Email** (Green theme)
   - Sent when attendees are first invited
   - Includes all event details
   - Welcome message

2. **Update Notification** (Orange theme)
   - Sent when event details are updated
   - Highlights the changes
   - Reminder to note the new details

3. **Cancellation Notification** (Red theme)
   - Sent when an event is cancelled
   - Apology for inconvenience
   - Original event details

## Testing

### Disable Email in Development

For local development, keep email disabled:

```properties
app.email.enabled=false
```

The system will log email attempts but won't actually send them.

### Enable Email in Production

In production, enable email sending:

```bash
export EMAIL_USERNAME=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password
export EMAIL_FROM=noreply@eventchey.com
export EMAIL_ENABLED=true
```

### Test Email Sending

1. Start the application with email enabled
2. Create an event
3. Invite an attendee with a real email address
4. Check the logs for email sending status
5. Verify the email was received

## Error Handling

- Email failures are logged but don't break the invitation process
- If email sending fails, the attendee is still added to the event
- Check application logs for email-related errors

## Security Considerations

1. **Never commit credentials**: Use environment variables for sensitive data
2. **App Passwords**: Use app-specific passwords instead of account passwords
3. **Rate Limiting**: Be aware of email provider sending limits
4. **SPF/DKIM**: Configure proper email authentication for production use

## Future Enhancements

Potential improvements for the email feature:

- [ ] Calendar invite attachments (.ics files)
- [ ] Email templates customization
- [ ] Bulk email optimization
- [ ] Email delivery tracking
- [ ] Unsubscribe functionality
- [ ] Email templates in multiple languages
- [ ] Rich text editor for event descriptions

## Troubleshooting

### Emails Not Sending

1. Check `app.email.enabled` is set to `true`
2. Verify email credentials are correct
3. Check SMTP host and port settings
4. Review application logs for error messages
5. Ensure firewall allows outbound SMTP traffic

### Gmail "Less Secure Apps" Error

Gmail no longer supports "less secure apps". You must:
1. Enable 2-factor authentication
2. Generate an App Password
3. Use the App Password instead of your account password

### Emails Going to Spam

1. Configure SPF records for your domain
2. Set up DKIM signing
3. Use a verified sender domain
4. Consider using a dedicated email service (SendGrid, AWS SES, etc.)

## API Documentation

The email feature is integrated into the existing Event API endpoints. See the Swagger UI at `/swagger-ui.html` for complete API documentation.

