# Event ID Scoping Update for Poll and Question Endpoints

## Summary
Updated all Poll and Question endpoints to use the `eventId` path parameter to properly scope operations to specific events. This ensures that polls and questions are only accessed within the context of their parent event, improving data integrity and security.

## Changes Made

### 1. Controller Layer Updates

#### PollController.java
- **createPoll**: Added `eventId` path variable and passed to service layer
- **submitVote**: Added `eventId` path variable and passed to service layer  
- **listPolls**: Added `eventId` path variable and passed to service layer

#### QuestionController.java
- **addQuestion**: Added `eventId` path variable and passed to service layer
- **answerQuestion**: Added `eventId` path variable and passed to service layer
- **listQuestions**: Added `eventId` path variable and passed to service layer

### 2. Service Interface Updates

#### PollService.java
Updated method signatures to include `eventId` parameter:
- `createPoll(String eventId, String agendaId, CreatePollRequest request)`
- `submitVote(String eventId, String agendaId, String pollId, SubmitVoteRequest request)`
- `listPolls(String eventId, String agendaId)`

#### QuestionService.java
Updated method signatures to include `eventId` parameter:
- `addQuestion(String eventId, String agendaId, AddQuestionRequest request)`
- `answerQuestion(String eventId, String agendaId, String questionId, AnswerQuestionRequest request)`
- `listQuestions(String eventId, String agendaId)`

### 3. Service Implementation Updates

#### PollServiceImpl.java
Changed from using `eventRepository.findEventByAgendaItemId(agendaId)` to `eventRepository.findById(eventId)`:
- **createPoll**: Now finds event by eventId, then validates agendaId belongs to that event
- **submitVote**: Now finds event by eventId, validates both agendaId and pollId belong to that event
- **listPolls**: Now finds event by eventId, then validates agendaId belongs to that event

#### QuestionServiceImpl.java
Changed from using `eventRepository.findEventByAgendaItemId(agendaId)` to `eventRepository.findById(eventId)`:
- **addQuestion**: Now finds event by eventId, then validates agendaId belongs to that event
- **answerQuestion**: Now finds event by eventId, validates both agendaId and questionId belong to that event
- **listQuestions**: Now finds event by eventId, then validates agendaId belongs to that event

## Benefits

1. **Improved Data Integrity**: Operations are now scoped to specific events, preventing cross-event data access
2. **Better Error Handling**: More specific error messages when event, agenda, poll, or question is not found
3. **Enhanced Security**: Ensures users can only access polls/questions within the event context they're authorized for
4. **Cleaner API Design**: The URL structure now clearly shows the hierarchical relationship: Event → Agenda → Poll/Question
5. **Consistent Pattern**: Aligns with REST API best practices where parent resources are referenced in the URL path

## API Endpoint Examples

### Poll Endpoints
```
POST   /api/events/{eventId}/agenda/{agendaId}/polls
POST   /api/events/{eventId}/agenda/{agendaId}/polls/{pollId}/vote
GET    /api/events/{eventId}/agenda/{agendaId}/polls
```

### Question Endpoints
```
POST   /api/events/{eventId}/agenda/{agendaId}/questions
POST   /api/events/{eventId}/agenda/{agendaId}/questions/{questionId}/answer
GET    /api/events/{eventId}/agenda/{agendaId}/questions
```

## Validation Flow

For each request, the system now:
1. Validates the event exists by `eventId`
2. Validates the agenda item exists within that event
3. For specific poll/question operations, validates they exist within that agenda item
4. Performs the requested operation
5. Returns appropriate error if any validation fails (ResourceNotFoundException)

## Compilation Status
✅ Project compiled successfully with no errors

