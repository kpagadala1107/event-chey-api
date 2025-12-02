# Email Invitation Examples

## Setup Instructions

### 1. Configure Email Settings

Add these environment variables before starting the application:

```bash
export EMAIL_USERNAME=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password
export EMAIL_FROM=noreply@eventchey.com
export EMAIL_ENABLED=true
```

Or update `application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
app.email.from=noreply@eventchey.com
app.email.enabled=true
```

### 2. Start the Application

```bash
./mvnw spring-boot:run
```

## API Usage Examples

### Create an Event

```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Annual Company Meeting",
    "description": "Our yearly all-hands company meeting to discuss goals and achievements",
    "startDate": "2025-12-15T10:00:00",
    "endDate": "2025-12-15T12:00:00",
    "createdBy": "admin@eventchey.com"
  }'
```

Response:
```json
{
  "id": "67890abc",
  "name": "Annual Company Meeting",
  "description": "Our yearly all-hands company meeting to discuss goals and achievements",
  "startDate": "2025-12-15T10:00:00",
  "endDate": "2025-12-15T12:00:00",
  "createdBy": "admin@eventchey.com",
  "attendees": [],
  "agenda": [],
  "createdAt": "2025-11-29T10:00:00",
  "updatedAt": "2025-11-29T10:00:00"
}
```

### Invite Attendees (Sends Email Automatically)

```bash
curl -X POST http://localhost:8080/api/events/67890abc/invite \
  -H "Content-Type: application/json" \
  -d '[
    {
      "email": "john.doe@company.com",
      "name": "John Doe",
      "phone": "+1-555-0123"
    },
    {
      "email": "jane.smith@company.com",
      "name": "Jane Smith",
      "phone": "+1-555-0124"
    },
    {
      "email": "bob.johnson@company.com",
      "name": "Bob Johnson",
      "phone": "+1-555-0125"
    }
  ]'
```

Response:
```json
{
  "id": "67890abc",
  "name": "Annual Company Meeting",
  "description": "Our yearly all-hands company meeting to discuss goals and achievements",
  "startDate": "2025-12-15T10:00:00",
  "endDate": "2025-12-15T12:00:00",
  "createdBy": "admin@eventchey.com",
  "attendees": [
    {
      "id": "att-001",
      "email": "john.doe@company.com",
      "name": "John Doe",
      "phone": "+1-555-0123",
      "status": "INVITED"
    },
    {
      "id": "att-002",
      "email": "jane.smith@company.com",
      "name": "Jane Smith",
      "phone": "+1-555-0124",
      "status": "INVITED"
    },
    {
      "id": "att-003",
      "email": "bob.johnson@company.com",
      "name": "Bob Johnson",
      "phone": "+1-555-0125",
      "status": "INVITED"
    }
  ],
  "agenda": [],
  "createdAt": "2025-11-29T10:00:00",
  "updatedAt": "2025-11-29T10:05:00"
}
```

### What the Attendees Receive

Each attendee will receive an HTML email with:

**Subject:** You're Invited: Annual Company Meeting

**Body:**
```
🎉 You're Invited!

Hi [Attendee Name],

You've been invited to attend the following event:

Annual Company Meeting

📅 Starts: December 15, 2025 at 10:00 AM
🏁 Ends: December 15, 2025 at 12:00 PM
📝 Description: Our yearly all-hands company meeting to discuss goals and achievements
👤 Organized by: admin@eventchey.com

We look forward to seeing you there!

Best regards,
Event Chey Team
```

## Testing Without Real Email

For testing without sending real emails, disable email:

```bash
export EMAIL_ENABLED=false
```

Then check the application logs to see what would have been sent:

```
INFO  c.k.e.s.i.EmailServiceImpl - Email sending is disabled. Skipping invitation email to: john.doe@company.com
INFO  c.k.e.s.i.EmailServiceImpl - Email sending is disabled. Skipping invitation email to: jane.smith@company.com
```

## Bulk Invitation Example

Invite multiple attendees in one request:

```bash
curl -X POST http://localhost:8080/api/events/67890abc/invite \
  -H "Content-Type: application/json" \
  -d '[
    {"email": "alice@company.com", "name": "Alice Cooper", "phone": "+1-555-0126"},
    {"email": "charlie@company.com", "name": "Charlie Brown", "phone": "+1-555-0127"},
    {"email": "diana@company.com", "name": "Diana Prince", "phone": "+1-555-0128"},
    {"email": "ethan@company.com", "name": "Ethan Hunt", "phone": "+1-555-0129"},
    {"email": "fiona@company.com", "name": "Fiona Gallagher", "phone": "+1-555-0130"}
  ]'
```

All 5 attendees will receive invitation emails simultaneously.

## Re-inviting Attendees

If you try to invite someone who's already been invited, they won't receive a duplicate email:

```bash
curl -X POST http://localhost:8080/api/events/67890abc/invite \
  -H "Content-Type: application/json" \
  -d '[
    {
      "email": "john.doe@company.com",
      "name": "John Doe",
      "phone": "+1-555-0123"
    }
  ]'
```

Log output:
```
WARN  c.k.e.s.i.EventServiceImpl - Attendee already invited: john.doe@company.com
```

## Checking Email Status

Review the application logs to confirm emails were sent:

```
INFO  c.k.e.s.i.EventServiceImpl - Inviting 3 attendees to event: 67890abc
INFO  c.k.e.s.i.EventServiceImpl - Added attendee: john.doe@company.com
INFO  c.k.e.s.i.EventServiceImpl - Added attendee: jane.smith@company.com
INFO  c.k.e.s.i.EventServiceImpl - Added attendee: bob.johnson@company.com
INFO  c.k.e.s.i.EventServiceImpl - Sending invitation emails to 3 new attendees
INFO  c.k.e.s.i.EmailServiceImpl - Invitation email sent successfully to: john.doe@company.com
INFO  c.k.e.s.i.EmailServiceImpl - Invitation email sent successfully to: jane.smith@company.com
INFO  c.k.e.s.i.EmailServiceImpl - Invitation email sent successfully to: bob.johnson@company.com
```

## Common Issues

### Email Not Received

1. Check spam/junk folder
2. Verify email address is correct
3. Check application logs for errors
4. Ensure EMAIL_ENABLED=true
5. Verify SMTP credentials

### Authentication Failed

Error: `535 Authentication failed`

Solution:
- For Gmail: Use an App Password, not your account password
- Enable 2-factor authentication first
- Generate App Password at: https://myaccount.google.com/apppasswords

### Connection Timeout

Error: `Connection timeout`

Solution:
- Check firewall settings
- Verify SMTP host and port
- Ensure outbound port 587 is open

## Next Steps

- Check the EMAIL_FEATURE.md file for complete documentation
- Configure production email service (SendGrid, AWS SES, etc.)
- Set up email templates customization
- Implement email delivery tracking

