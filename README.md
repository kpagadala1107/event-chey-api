# Event Chey API

A production-quality Spring Boot 3 application for event management with AI integration.

## Features

- **Event Management**: Create, update, and manage events
- **Invitations**: Invite attendees to events
- **Email Invitations**: Automatically send professional HTML email invitations to attendees
- **Agenda Timeline**: Create and manage agenda items with scheduling
- **Q&A System**: Ask questions and get speaker answers with upvoting
- **Live Polls**: Create polls and collect votes with real-time aggregation
- **AI Summaries**: Generate AI-powered summaries of events, agenda items, and Q&A sessions

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data MongoDB**
- **Spring Validation**
- **Spring Security** (with JWT support)
- **MapStruct** (for DTO mappings)
- **OpenAPI/Swagger** (API documentation)
- **NO LOMBOK** (explicit constructors, getters, setters)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+ (running locally on port 27017)

## Installation

1. **Clone the repository** (or extract the project)

2. **Install MongoDB** (if not already installed)
   ```bash
   # macOS
   brew tap mongodb/brew
   brew install mongodb-community
   brew services start mongodb-community
   
   # Verify MongoDB is running
   mongosh
   ```

3. **Build the project**
   ```bash
   cd event-chey-api
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

   The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## API Endpoints

### Event Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/events` | Create a new event |
| GET | `/events/{id}` | Get event by ID |
| PUT | `/events/{id}` | Update an event |
| POST | `/events/{id}/invite` | Invite attendees to an event |
| GET | `/events?createdBy=&from=&to=` | List events with filters |

### Agenda Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/events/{eventId}/agenda` | Add agenda item to an event |
| GET | `/events/{eventId}/agenda` | Get all agenda items |

### Question & Answer

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/agenda/{agendaId}/questions` | Add a question |
| POST | `/agenda/{agendaId}/questions/{questionId}/answer` | Answer a question |
| GET | `/agenda/{agendaId}/questions` | List all questions |

### Polls

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/agenda/{agendaId}/polls` | Create a poll |
| POST | `/agenda/{agendaId}/polls/{pollId}/vote` | Submit a vote |
| GET | `/agenda/{agendaId}/polls` | List all polls |

## Sample API Requests

### 1. Create an Event

```bash
curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Spring Boot Conference 2025",
    "description": "Annual conference for Spring Boot developers",
    "startDate": "2025-12-01T09:00:00",
    "endDate": "2025-12-01T17:00:00",
    "createdBy": "john.doe@example.com"
  }'
```

### 2. Invite Attendees

```bash
curl -X POST http://localhost:8080/events/{eventId}/invite \
  -H "Content-Type: application/json" \
  -d '[
    {
      "email": "jane.smith@example.com",
      "phone": "+1234567890",
      "name": "Jane Smith"
    },
    {
      "email": "bob.johnson@example.com",
      "phone": "+1987654321",
      "name": "Bob Johnson"
    }
  ]'
```

### 3. Add Agenda Item

```bash
curl -X POST http://localhost:8080/events/{eventId}/agenda \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Introduction to Spring Boot 3",
    "startTime": "2025-12-01T10:00:00",
    "endTime": "2025-12-01T11:00:00",
    "description": "Learn about new features in Spring Boot 3",
    "speaker": "John Doe"
  }'
```

### 4. Add a Question

```bash
curl -X POST http://localhost:8080/agenda/{agendaId}/questions \
  -H "Content-Type: application/json" \
  -d '{
    "attendeeId": "attendee-123",
    "questionText": "What are the performance improvements in Spring Boot 3?"
  }'
```

### 5. Create a Poll

```bash
curl -X POST http://localhost:8080/agenda/{agendaId}/polls \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Which Spring feature are you most excited about?",
    "options": [
      "Native Image Support",
      "Observability",
      "Virtual Threads",
      "GraalVM"
    ]
  }'
```

### 6. Submit a Vote

```bash
curl -X POST http://localhost:8080/agenda/{agendaId}/polls/{pollId}/vote \
  -H "Content-Type: application/json" \
  -d '{
    "option": "Native Image Support"
  }'
```

## MongoDB Sample Documents

### Event Document

```json
{
  "_id": "6567890abcdef1234567890a",
  "name": "Spring Boot Conference 2025",
  "description": "Annual conference for Spring Boot developers",
  "startDate": "2025-12-01T09:00:00",
  "endDate": "2025-12-01T17:00:00",
  "createdBy": "john.doe@example.com",
  "attendees": [
    {
      "id": "att-001",
      "email": "jane.smith@example.com",
      "phone": "+1234567890",
      "name": "Jane Smith",
      "status": "ACCEPTED"
    }
  ],
  "agenda": [
    {
      "id": "agenda-001",
      "title": "Introduction to Spring Boot 3",
      "startTime": "2025-12-01T10:00:00",
      "endTime": "2025-12-01T11:00:00",
      "description": "Learn about new features in Spring Boot 3",
      "speaker": "John Doe",
      "questions": [
        {
          "id": "q-001",
          "attendeeId": "att-001",
          "questionText": "What are the performance improvements?",
          "answerText": "Spring Boot 3 includes native compilation support...",
          "timestamp": "2025-12-01T10:30:00",
          "upvotes": 5
        }
      ],
      "polls": [
        {
          "id": "poll-001",
          "question": "Which Spring feature are you most excited about?",
          "options": [
            "Native Image Support",
            "Observability",
            "Virtual Threads",
            "GraalVM"
          ],
          "votes": {
            "Native Image Support": 15,
            "Observability": 8,
            "Virtual Threads": 12,
            "GraalVM": 5
          }
        }
      ],
      "aiSummary": "AI Summary not available in development mode..."
    }
  ],
  "createdAt": "2025-11-15T08:00:00",
  "updatedAt": "2025-11-19T14:30:00",
  "_class": "com.kp.eventchey.domain.Event"
}
```

## Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=eventchey

# Server Configuration
server.port=8080

# JWT Configuration (optional)
jwt.secret=your-secret-key
jwt.expiration=86400000

# Email Configuration (for invitation feature)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
app.email.from=${EMAIL_FROM:noreply@eventchey.com}
app.email.enabled=${EMAIL_ENABLED:false}
```

### Email Invitations Setup

To enable email invitations, configure the following environment variables:

```bash
export EMAIL_USERNAME=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password
export EMAIL_FROM=noreply@eventchey.com
export EMAIL_ENABLED=true
```

Or use the setup script:
```bash
./setup-email.sh
```

For detailed email configuration and troubleshooting, see:
- **[EMAIL_FEATURE.md](EMAIL_FEATURE.md)** - Complete feature documentation
- **[EMAIL_EXAMPLES.md](EMAIL_EXAMPLES.md)** - Usage examples and API requests
- **[EMAIL_IMPLEMENTATION_SUMMARY.md](EMAIL_IMPLEMENTATION_SUMMARY.md)** - Implementation details

## Project Structure

```
com.kp.eventchey
├── EventCheyApiApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── OpenApiConfig.java
├── controller/
│   ├── EventController.java
│   ├── AgendaController.java
│   ├── QuestionController.java
│   └── PollController.java
├── service/
│   ├── EventService.java
│   ├── AgendaService.java
│   ├── QuestionService.java
│   ├── PollService.java
│   └── impl/
│       ├── EventServiceImpl.java
│       ├── AgendaServiceImpl.java
│       ├── QuestionServiceImpl.java
│       └── PollServiceImpl.java
├── repository/
│   └── EventRepository.java
├── dto/
│   ├── request/
│   │   ├── CreateEventRequest.java
│   │   ├── UpdateEventRequest.java
│   │   ├── InviteAttendeeRequest.java
│   │   ├── AddAgendaItemRequest.java
│   │   ├── AddQuestionRequest.java
│   │   ├── AnswerQuestionRequest.java
│   │   ├── CreatePollRequest.java
│   │   └── SubmitVoteRequest.java
│   └── response/
│       ├── EventResponse.java
│       ├── AgendaItemResponse.java
│       ├── AttendeeResponse.java
│       ├── QuestionResponse.java
│       └── PollResponse.java
├── domain/
│   ├── Event.java
│   ├── AgendaItem.java
│   ├── Attendee.java
│   ├── AttendeeStatus.java
│   ├── Question.java
│   └── Poll.java
├── mapper/
│   ├── EventMapper.java
│   ├── AgendaItemMapper.java
│   ├── AttendeeMapper.java
│   ├── QuestionMapper.java
│   └── PollMapper.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   ├── ValidationException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
└── ai/
    ├── AiSummaryService.java
    └── impl/
        └── OpenAiSummaryService.java
```

## AI Integration

The application includes a stub AI summary service that can be extended to integrate with actual AI providers:

- **OpenAI GPT**
- **Google Vertex AI**
- **AWS Bedrock**
- **Azure OpenAI**

Currently, the `OpenAiSummaryService` returns placeholder text for development purposes.

## Testing

Run the tests:

```bash
./mvnw test
```

## Building for Production

Create a production build:

```bash
./mvnw clean package -DskipTests
```

The JAR file will be created in `target/event-chey-api-0.0.1-SNAPSHOT.jar`

Run the production build:

```bash
java -jar target/event-chey-api-0.0.1-SNAPSHOT.jar
```

## Notes

- **NO LOMBOK**: All classes use explicit constructors, getters, setters, equals, hashCode, and toString methods
- **Java Records**: Used for DTOs (Request/Response objects)
- **MapStruct**: Automatic DTO-Entity mapping generation
- **MongoDB**: Embedded document structure for performance
- **Security**: Basic Spring Security configuration (can be extended for JWT)
- **Validation**: Jakarta Bean Validation annotations on request DTOs

## License

Apache 2.0

## Support

For issues and questions, please contact: support@eventchey.com

