# AI-Powered Agenda Generation Feature

## Overview
Implemented automatic agenda generation for events upon creation. When a new event is created, the system uses OpenAI's LLM to intelligently generate a comprehensive agenda based on the event details.

## Changes Made

### 1. LLMService.java
**Location:** `/src/main/java/com/kp/eventchey/service/LLMService.java`

Added new method:
- `generateAgenda(String eventName, String eventDescription, String startDate, String endDate)`: 
  - Takes event details as input
  - Uses OpenAI GPT to generate a realistic, comprehensive agenda
  - Returns agenda as JSON array with items containing:
    - `title`: Agenda item title
    - `startTime`: ISO 8601 formatted datetime
    - `endTime`: ISO 8601 formatted datetime
    - `description`: Brief description
    - `speaker`: Suggested speaker or "TBD"
  - Considers event duration (single or multi-day)
  - Includes standard event elements (registration, breaks, meals, networking, closing)

### 2. EventServiceImpl.java
**Location:** `/src/main/java/com/kp/eventchey/service/impl/EventServiceImpl.java`

#### Updated Dependencies:
- Added `LLMService` dependency injection
- Added Jackson imports for JSON parsing (`ObjectMapper`, `TypeReference`, `JavaTimeModule`)
- Added `DateTimeFormatter` for date formatting

#### Modified `createEvent()` Method:
The event creation flow now:
1. Validates event dates
2. Creates the basic event entity
3. **NEW:** Automatically generates agenda using AI:
   - Calls `llmService.generateAgenda()` with event details
   - Parses the JSON response
   - Creates `AgendaItem` objects with unique IDs
   - Attaches agenda items to the event
4. Saves the event with generated agenda to database
5. Returns the complete event response

#### Added Helper Method:
- `parseAgendaFromJson(String json)`:
  - Cleans JSON response (removes markdown code blocks if present)
  - Parses JSON array into `List<Map<String, Object>>`
  - Converts each item into an `AgendaItem` domain object
  - Handles LocalDateTime parsing for start/end times
  - Generates unique UUID for each agenda item
  - Gracefully handles parsing errors by returning empty list

### 3. Error Handling
- AI agenda generation is wrapped in try-catch
- If generation fails, event creation continues with an empty agenda
- Logs are added for tracking agenda generation process

## How It Works

### Event Creation Flow:
```
1. User submits CreateEventRequest
   ├─ name
   ├─ description
   ├─ startDate
   ├─ endDate
   └─ createdBy

2. EventServiceImpl.createEvent()
   ├─ Validates dates
   ├─ Creates Event entity
   └─ Generates Agenda via AI
      ├─ LLMService.generateAgenda()
      │  ├─ Constructs prompt with event details
      │  ├─ Calls OpenAI API
      │  └─ Returns JSON agenda
      └─ parseAgendaFromJson()
         ├─ Cleans JSON response
         ├─ Parses to AgendaItem objects
         └─ Assigns to event.agenda

3. Saves complete event to MongoDB

4. Returns EventResponse with generated agenda
```

### AI Prompt Strategy:
The system prompt instructs the AI to:
- Act as an expert event planner
- Consider event type, duration, and purpose
- Include realistic timing and breaks
- Create logical flow throughout the event
- Return structured JSON format

### Example Generated Agenda Items:
For a 2-day tech conference:
- Day 1: Registration, Opening Keynote, Technical Sessions, Lunch, Workshops, Networking
- Day 2: Breakfast, Panel Discussions, Breakout Sessions, Lunch, Closing Remarks

## Benefits

1. **Time Savings**: Eliminates manual agenda creation
2. **Consistency**: Professional structure for all events
3. **Intelligence**: AI adapts to event type and duration
4. **Flexibility**: Generated agenda can be edited later
5. **Robustness**: Graceful fallback if AI generation fails
6. **User Experience**: Events come with ready-to-use agendas

## API Response Example

When creating an event, the response now includes a populated `agenda` array:

```json
{
  "id": "event-123",
  "name": "Tech Conference 2025",
  "description": "Annual technology conference",
  "startDate": "2025-06-15T09:00:00",
  "endDate": "2025-06-16T17:00:00",
  "createdBy": "user@example.com",
  "agenda": [
    {
      "id": "agenda-1",
      "title": "Registration & Welcome Coffee",
      "startTime": "2025-06-15T09:00:00",
      "endTime": "2025-06-15T09:30:00",
      "description": "Check-in and networking",
      "speaker": "TBD"
    },
    {
      "id": "agenda-2",
      "title": "Opening Keynote",
      "startTime": "2025-06-15T09:30:00",
      "endTime": "2025-06-15T10:30:00",
      "description": "Welcome address and conference overview",
      "speaker": "CEO"
    }
    // ... more agenda items
  ],
  "attendees": [],
  "createdAt": "2025-12-03T23:30:00",
  "updatedAt": "2025-12-03T23:30:00"
}
```

## Configuration

Ensure these properties are set in `application.properties`:
```properties
openai.api.key=your-api-key-here
openai.model=gpt-4
```

## Future Enhancements

Potential improvements:
1. Allow users to opt-out of auto-generation
2. Add agenda templates for common event types
3. Support custom agenda generation prompts
4. Regenerate agenda if event dates change significantly
5. Add AI-powered agenda optimization suggestions

