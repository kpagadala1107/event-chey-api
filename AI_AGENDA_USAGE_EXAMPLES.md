# AI Agenda Generation - Usage Examples

## Testing the Feature

### 1. Create an Event with Auto-Generated Agenda

**Endpoint:** `POST /api/events`

**Request Body:**
```json
{
  "name": "Spring Tech Summit 2025",
  "description": "A comprehensive two-day conference focused on Spring Framework, Microservices, and Cloud Native Development. Sessions include hands-on workshops, expert panels, and networking opportunities for Java developers.",
  "startDate": "2025-06-15T09:00:00",
  "endDate": "2025-06-16T17:00:00",
  "createdBy": "admin@techsummit.com"
}
```

**Expected Response:**
The API will automatically generate a comprehensive agenda and return:
```json
{
  "id": "675f3a21e8c9f0001a4b2c3d",
  "name": "Spring Tech Summit 2025",
  "description": "A comprehensive two-day conference...",
  "startDate": "2025-06-15T09:00:00",
  "endDate": "2025-06-16T17:00:00",
  "createdBy": "admin@techsummit.com",
  "attendees": [],
  "agenda": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "title": "Registration & Welcome Coffee",
      "startTime": "2025-06-15T09:00:00",
      "endTime": "2025-06-15T09:30:00",
      "description": "Attendee check-in and morning refreshments",
      "speaker": "TBD",
      "questions": [],
      "polls": [],
      "aiSummary": null
    },
    {
      "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "title": "Opening Keynote: The Future of Spring",
      "startTime": "2025-06-15T09:30:00",
      "endTime": "2025-06-15T10:30:00",
      "description": "Conference kickoff with vision for Spring Framework evolution",
      "speaker": "Senior Spring Architect",
      "questions": [],
      "polls": [],
      "aiSummary": null
    },
    {
      "id": "c3d4e5f6-a7b8-9012-cdef-123456789012",
      "title": "Microservices with Spring Boot",
      "startTime": "2025-06-15T10:45:00",
      "endTime": "2025-06-15T12:00:00",
      "description": "Building scalable microservices architecture",
      "speaker": "Technical Lead",
      "questions": [],
      "polls": [],
      "aiSummary": null
    },
    {
      "id": "d4e5f6a7-b8c9-0123-def1-234567890123",
      "title": "Lunch Break & Networking",
      "startTime": "2025-06-15T12:00:00",
      "endTime": "2025-06-15T13:00:00",
      "description": "Catered lunch with networking opportunities",
      "speaker": "TBD",
      "questions": [],
      "polls": [],
      "aiSummary": null
    }
    // ... more agenda items for Day 1 and Day 2
  ],
  "createdAt": "2025-12-04T00:00:00",
  "updatedAt": "2025-12-04T00:00:00",
  "cachedAiSummary": null,
  "aiSummaryGeneratedAt": null
}
```

### 2. Single-Day Workshop Example

**Request:**
```json
{
  "name": "Docker & Kubernetes Workshop",
  "description": "Hands-on workshop covering containerization with Docker and orchestration with Kubernetes. Includes practical exercises and real-world deployment scenarios.",
  "startDate": "2025-05-20T10:00:00",
  "endDate": "2025-05-20T17:00:00",
  "createdBy": "trainer@devops.com"
}
```

**Generated Agenda (Expected):**
- 10:00-10:30: Introduction & Environment Setup
- 10:30-12:00: Docker Fundamentals & Container Creation
- 12:00-13:00: Lunch Break
- 13:00-14:30: Docker Compose & Multi-Container Apps
- 14:30-14:45: Coffee Break
- 14:45-16:00: Kubernetes Basics & Pod Deployment
- 16:00-17:00: Advanced Topics & Q&A

### 3. Multi-Day Conference Example

**Request:**
```json
{
  "name": "AI & Machine Learning Summit 2025",
  "description": "Three-day conference exploring latest advances in AI, machine learning, deep learning, and practical applications in enterprise environments.",
  "startDate": "2025-09-10T09:00:00",
  "endDate": "2025-09-12T17:00:00",
  "createdBy": "organizer@aisummit.com"
}
```

**Generated Agenda Structure (Expected):**

**Day 1 (Sep 10):**
- Morning: Registration, Opening Keynote, AI Fundamentals
- Afternoon: Deep Learning Sessions, Workshops, Networking

**Day 2 (Sep 11):**
- Morning: Advanced ML Topics, Panel Discussions
- Afternoon: Hands-on Labs, Use Case Presentations

**Day 3 (Sep 12):**
- Morning: Enterprise AI Implementation, Best Practices
- Afternoon: Closing Sessions, Future Outlook, Wrap-up

### 4. Short Evening Event Example

**Request:**
```json
{
  "name": "Product Launch Evening",
  "description": "Evening event to unveil our new SaaS platform with live demos, Q&A session, and networking reception.",
  "startDate": "2025-07-15T18:00:00",
  "endDate": "2025-07-15T21:00:00",
  "createdBy": "marketing@company.com"
}
```

**Generated Agenda (Expected):**
- 18:00-18:30: Welcome Reception & Refreshments
- 18:30-19:00: Company Overview & Product Introduction
- 19:00-19:30: Live Product Demo
- 19:30-20:00: Q&A Session
- 20:00-21:00: Networking & Cocktails

## Testing with curl

```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "DevOps Conference 2025",
    "description": "Annual DevOps conference covering CI/CD, automation, and cloud infrastructure",
    "startDate": "2025-08-20T09:00:00",
    "endDate": "2025-08-21T17:00:00",
    "createdBy": "admin@devopsconf.com"
  }'
```

## Viewing Generated Agenda

After creating the event, you can view the full agenda by:

1. Getting the event by ID:
```bash
curl http://localhost:8080/api/events/{eventId}
```

2. The response will include the complete `agenda` array with all generated items.

## Important Notes

1. **AI Generation Time**: The first request may take 5-10 seconds as it calls OpenAI's API
2. **Fallback**: If AI generation fails, event is still created with empty agenda
3. **Editable**: Generated agenda items can be modified via the Agenda API endpoints
4. **Context-Aware**: AI considers event name, description, and duration to create relevant agenda
5. **Consistent Format**: All agenda items include title, time range, description, and suggested speaker

## Logs to Monitor

When creating an event, check logs for:
```
INFO - Creating event: Spring Tech Summit 2025
INFO - Generating agenda for event: Spring Tech Summit 2025
INFO - Generated 12 agenda items for event
INFO - Event created with ID: 675f3a21e8c9f0001a4b2c3d
```

If agenda generation fails:
```
ERROR - Failed to generate agenda for event: Spring Tech Summit 2025
```
(Event will still be created successfully)

## Customization

To customize the agenda generation prompt, edit the `systemPrompt` in:
`src/main/java/com/kp/eventchey/service/LLMService.java`

The current prompt emphasizes:
- Realistic event structure
- Appropriate time allocations
- Standard event practices (breaks, meals, networking)
- Logical flow throughout the event

