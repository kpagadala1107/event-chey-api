# API Testing Examples

## Create Event

### Using cURL

```bash
curl -X POST "http://localhost:8080/events" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Conference 2025",
    "description": "Annual technology conference",
    "startDate": "2025-12-01T09:00:00",
    "endDate": "2025-12-01T17:00:00",
    "createdBy": "admin@example.com"
  }'
```

### From React/JavaScript

```javascript
// Using fetch
fetch('http://localhost:8080/events', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: 'include',
  body: JSON.stringify({
    name: 'Tech Conference 2025',
    description: 'Annual technology conference',
    startDate: '2025-12-01T09:00:00',
    endDate: '2025-12-01T17:00:00',
    createdBy: 'admin@example.com'
  })
})
.then(response => response.json())
.then(data => console.log('Event created:', data))
.catch(error => console.error('Error:', error));

// Using axios
import axios from 'axios';

axios.post('http://localhost:8080/events', {
  name: 'Tech Conference 2025',
  description: 'Annual technology conference',
  startDate: '2025-12-01T09:00:00',
  endDate: '2025-12-01T17:00:00',
  createdBy: 'admin@example.com'
}, {
  withCredentials: true
})
.then(response => console.log('Event created:', response.data))
.catch(error => console.error('Error:', error));
```

## Get Event by ID

```bash
curl -X GET "http://localhost:8080/events/{eventId}"
```

## Update Event

```bash
curl -X PUT "http://localhost:8080/events/{eventId}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Conference Name",
    "description": "Updated description",
    "startDate": "2025-12-01T10:00:00",
    "endDate": "2025-12-01T18:00:00"
  }'
```

## Invite Attendees

```bash
curl -X POST "http://localhost:8080/events/{eventId}/invite" \
  -H "Content-Type: application/json" \
  -d '[
    {
      "email": "john.doe@example.com",
      "phone": "+1234567890",
      "name": "John Doe"
    },
    {
      "email": "jane.smith@example.com",
      "phone": "+1987654321",
      "name": "Jane Smith"
    }
  ]'
```

## List Events

```bash
# All events
curl -X GET "http://localhost:8080/events"

# By creator
curl -X GET "http://localhost:8080/events?createdBy=admin@example.com"

# By date range
curl -X GET "http://localhost:8080/events?from=2025-12-01T00:00:00&to=2025-12-31T23:59:59"

# Combined filters
curl -X GET "http://localhost:8080/events?createdBy=admin@example.com&from=2025-12-01T00:00:00&to=2025-12-31T23:59:59"
```

## Add Agenda Item

```bash
curl -X POST "http://localhost:8080/events/{eventId}/agenda" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Opening Keynote",
    "startTime": "2025-12-01T09:00:00",
    "endTime": "2025-12-01T10:00:00",
    "description": "Welcome and introduction to the conference",
    "speaker": "Dr. John Speaker"
  }'
```

## Add Question

```bash
curl -X POST "http://localhost:8080/agenda/{agendaId}/questions" \
  -H "Content-Type: application/json" \
  -d '{
    "attendeeId": "att-001",
    "questionText": "What are the main topics covered in this session?"
  }'
```

## Answer Question

```bash
curl -X POST "http://localhost:8080/agenda/{agendaId}/questions/{questionId}/answer" \
  -H "Content-Type: application/json" \
  -d '{
    "answerText": "We will cover Spring Boot 3, microservices, and cloud deployment strategies."
  }'
```

## Create Poll

```bash
curl -X POST "http://localhost:8080/agenda/{agendaId}/polls" \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Which topic interests you most?",
    "options": [
      "Microservices",
      "Cloud Native",
      "AI/ML Integration",
      "Security"
    ]
  }'
```

## Submit Vote

```bash
curl -X POST "http://localhost:8080/agenda/{agendaId}/polls/{pollId}/vote" \
  -H "Content-Type: application/json" \
  -d '{
    "option": "Microservices"
  }'
```

## Get All Polls

```bash
curl -X GET "http://localhost:8080/agenda/{agendaId}/polls"
```

## Notes

- Replace `{eventId}`, `{agendaId}`, `{questionId}`, `{pollId}` with actual IDs from responses
- Dates must be in ISO 8601 format: `YYYY-MM-DDTHH:mm:ss`
- Dates must be in the future for event creation (after November 20, 2025)
- All endpoints support CORS from localhost:3000

