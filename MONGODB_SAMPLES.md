# MongoDB Sample Data for Event Chey API

## Collection: events

### Sample Event Document

```javascript
db.events.insertOne({
  "name": "Spring Boot Conference 2025",
  "description": "Annual conference for Spring Boot developers with cutting-edge sessions",
  "startDate": ISODate("2025-12-01T09:00:00Z"),
  "endDate": ISODate("2025-12-01T17:00:00Z"),
  "createdBy": "john.doe@example.com",
  "attendees": [
    {
      "id": "att-001",
      "email": "jane.smith@example.com",
      "phone": "+1-555-0101",
      "name": "Jane Smith",
      "status": "ACCEPTED"
    },
    {
      "id": "att-002",
      "email": "bob.johnson@example.com",
      "phone": "+1-555-0102",
      "name": "Bob Johnson",
      "status": "INVITED"
    },
    {
      "id": "att-003",
      "email": "alice.williams@example.com",
      "phone": "+1-555-0103",
      "name": "Alice Williams",
      "status": "ACCEPTED"
    }
  ],
  "agenda": [
    {
      "id": "agenda-001",
      "title": "Introduction to Spring Boot 3",
      "startTime": ISODate("2025-12-01T10:00:00Z"),
      "endTime": ISODate("2025-12-01T11:00:00Z"),
      "description": "Learn about new features and improvements in Spring Boot 3",
      "speaker": "John Doe",
      "questions": [
        {
          "id": "q-001",
          "attendeeId": "att-001",
          "questionText": "What are the performance improvements in Spring Boot 3?",
          "answerText": "Spring Boot 3 includes native compilation support with GraalVM, improved startup time, and reduced memory footprint.",
          "timestamp": ISODate("2025-12-01T10:30:00Z"),
          "upvotes": 5
        },
        {
          "id": "q-002",
          "attendeeId": "att-003",
          "questionText": "Is there backward compatibility with Spring Boot 2?",
          "answerText": "Spring Boot 3 requires Java 17 and has some breaking changes. Migration guides are available.",
          "timestamp": ISODate("2025-12-01T10:35:00Z"),
          "upvotes": 3
        }
      ],
      "polls": [
        {
          "id": "poll-001",
          "question": "Which Spring Boot 3 feature are you most excited about?",
          "options": [
            "Native Image Support",
            "Observability",
            "Virtual Threads",
            "GraalVM Integration"
          ],
          "votes": {
            "Native Image Support": 15,
            "Observability": 8,
            "Virtual Threads": 12,
            "GraalVM Integration": 5
          }
        }
      ],
      "aiSummary": "AI Summary not available in development mode. Agenda: 'Introduction to Spring Boot 3' by John Doe (Duration: 60 minutes)"
    },
    {
      "id": "agenda-002",
      "title": "Reactive Programming with Spring WebFlux",
      "startTime": ISODate("2025-12-01T11:15:00Z"),
      "endTime": ISODate("2025-12-01T12:15:00Z"),
      "description": "Deep dive into reactive programming patterns",
      "speaker": "Sarah Connor",
      "questions": [
        {
          "id": "q-003",
          "attendeeId": "att-002",
          "questionText": "When should we use WebFlux over Spring MVC?",
          "answerText": null,
          "timestamp": ISODate("2025-12-01T11:45:00Z"),
          "upvotes": 2
        }
      ],
      "polls": [],
      "aiSummary": "AI Summary not available in development mode. Agenda: 'Reactive Programming with Spring WebFlux' by Sarah Connor (Duration: 60 minutes)"
    },
    {
      "id": "agenda-003",
      "title": "Microservices Architecture Best Practices",
      "startTime": ISODate("2025-12-01T14:00:00Z"),
      "endTime": ISODate("2025-12-01T15:30:00Z"),
      "description": "Learn about microservices design patterns and anti-patterns",
      "speaker": "Mike Wilson",
      "questions": [],
      "polls": [
        {
          "id": "poll-002",
          "question": "What's your biggest challenge with microservices?",
          "options": [
            "Service Discovery",
            "Distributed Tracing",
            "Data Consistency",
            "Testing"
          ],
          "votes": {
            "Service Discovery": 6,
            "Distributed Tracing": 9,
            "Data Consistency": 18,
            "Testing": 7
          }
        }
      ],
      "aiSummary": "AI Summary not available in development mode. Agenda: 'Microservices Architecture Best Practices' by Mike Wilson (Duration: 90 minutes)"
    }
  ],
  "createdAt": ISODate("2025-11-15T08:00:00Z"),
  "updatedAt": ISODate("2025-11-19T14:30:00Z"),
  "_class": "com.kp.eventchey.domain.Event"
});

// Insert another sample event
db.events.insertOne({
  "name": "Tech Innovation Summit 2025",
  "description": "Exploring the future of technology and innovation",
  "startDate": ISODate("2025-11-25T08:00:00Z"),
  "endDate": ISODate("2025-11-25T18:00:00Z"),
  "createdBy": "jane.admin@example.com",
  "attendees": [
    {
      "id": "att-004",
      "email": "tech.enthusiast@example.com",
      "phone": "+1-555-0201",
      "name": "Tech Enthusiast",
      "status": "ACCEPTED"
    }
  ],
  "agenda": [
    {
      "id": "agenda-004",
      "title": "AI and Machine Learning Trends",
      "startTime": ISODate("2025-11-25T09:00:00Z"),
      "endTime": ISODate("2025-11-25T10:30:00Z"),
      "description": "Latest developments in AI and ML",
      "speaker": "Dr. Emily Chen",
      "questions": [],
      "polls": [],
      "aiSummary": "AI Summary not available in development mode. Agenda: 'AI and Machine Learning Trends' by Dr. Emily Chen (Duration: 90 minutes)"
    }
  ],
  "createdAt": ISODate("2025-11-10T10:00:00Z"),
  "updatedAt": ISODate("2025-11-19T09:00:00Z"),
  "_class": "com.kp.eventchey.domain.Event"
});
```

## MongoDB Queries

### Find all events
```javascript
db.events.find().pretty();
```

### Find events by creator
```javascript
db.events.find({ "createdBy": "john.doe@example.com" });
```

### Find events by attendee email
```javascript
db.events.find({ "attendees.email": "jane.smith@example.com" });
```

### Find events within date range
```javascript
db.events.find({
  "startDate": {
    $gte: ISODate("2025-11-01T00:00:00Z"),
    $lte: ISODate("2025-12-31T23:59:59Z")
  }
});
```

### Find event by agenda item ID
```javascript
db.events.findOne({ "agenda.id": "agenda-001" });
```

### Update attendee status
```javascript
db.events.updateOne(
  { "_id": ObjectId("your-event-id"), "attendees.id": "att-002" },
  { $set: { "attendees.$.status": "ACCEPTED" } }
);
```

### Add upvote to a question
```javascript
db.events.updateOne(
  { "agenda.id": "agenda-001", "agenda.questions.id": "q-001" },
  { $inc: { "agenda.$[agenda].questions.$[question].upvotes": 1 } },
  { arrayFilters: [
    { "agenda.id": "agenda-001" },
    { "question.id": "q-001" }
  ]}
);
```

## Initialize MongoDB Database

Run these commands in MongoDB shell:

```javascript
// Switch to eventchey database
use eventchey;

// Create indexes for better performance
db.events.createIndex({ "createdBy": 1 });
db.events.createIndex({ "attendees.email": 1 });
db.events.createIndex({ "startDate": 1 });
db.events.createIndex({ "agenda.id": 1 });

// Insert sample data
// (Use the insertOne commands above)

// Verify data
db.events.countDocuments();
```

## Sample JSON for API Testing (Postman/cURL)

### Create Event Request
```json
{
  "name": "Spring Boot Conference 2025",
  "description": "Annual conference for Spring Boot developers",
  "startDate": "2025-12-01T09:00:00",
  "endDate": "2025-12-01T17:00:00",
  "createdBy": "john.doe@example.com"
}
```

### Invite Attendees Request
```json
[
  {
    "email": "jane.smith@example.com",
    "phone": "+1-555-0101",
    "name": "Jane Smith"
  },
  {
    "email": "bob.johnson@example.com",
    "phone": "+1-555-0102",
    "name": "Bob Johnson"
  }
]
```

### Add Agenda Item Request
```json
{
  "title": "Introduction to Spring Boot 3",
  "startTime": "2025-12-01T10:00:00",
  "endTime": "2025-12-01T11:00:00",
  "description": "Learn about new features in Spring Boot 3",
  "speaker": "John Doe"
}
```

### Add Question Request
```json
{
  "attendeeId": "att-001",
  "questionText": "What are the performance improvements in Spring Boot 3?"
}
```

### Answer Question Request
```json
{
  "answerText": "Spring Boot 3 includes native compilation support with GraalVM, improved startup time, and reduced memory footprint."
}
```

### Create Poll Request
```json
{
  "question": "Which Spring feature are you most excited about?",
  "options": [
    "Native Image Support",
    "Observability",
    "Virtual Threads",
    "GraalVM Integration"
  ]
}
```

### Submit Vote Request
```json
{
  "option": "Native Image Support"
}
```

