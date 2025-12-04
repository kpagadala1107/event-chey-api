# Timezone Consistency in Agenda Generation

## Overview
The AI-powered agenda generation feature ensures that all generated agenda items maintain the **same timezone context** as the event's start and end times.

## Problem Statement
When generating agenda items using AI, it's critical that:
1. Agenda item times are in the same timezone as the event
2. Agenda items fall within the event's time boundaries
3. No unintended timezone conversions occur during generation or parsing

## Implementation

### 1. LLM Prompt Engineering
The AI prompt explicitly instructs the language model to maintain timezone consistency:

**System Prompt:**
```
CRITICAL: All agenda item times MUST be in the SAME TIMEZONE as the event start and end times.
Use the EXACT date-time format provided in the event times, maintaining the same timezone context.
Do NOT convert times or change timezones. Keep all times consistent with the event's timezone.
```

**User Prompt:**
```
IMPORTANT: Generate all agenda item times using the SAME timezone as the event times above.
All startTime and endTime values must be between the event start and end times shown above.
Maintain the exact date-time format (yyyy-MM-dd'T'HH:mm:ss) without any timezone conversions.
```

### 2. Format Specification
All times use **ISO 8601 Local DateTime format**: `yyyy-MM-dd'T'HH:mm:ss`

This format:
- Represents a date and time without timezone offset
- Prevents automatic timezone conversions
- Maintains consistency with the event's timezone context
- Example: `2025-06-15T09:00:00`

### 3. Validation During Parsing
The `parseAgendaFromJson()` method includes validation to ensure:
- Agenda item start times are within event boundaries
- Agenda item end times are within event boundaries
- Warning logs are generated for any items outside boundaries

**Code:**
```java
// Validate time is within event boundaries
if (startTime.isBefore(eventStart) || startTime.isAfter(eventEnd)) {
    logger.warn("Agenda item '{}' start time {} is outside event boundaries [{}, {}]",
        item.getTitle(), startTime, eventStart, eventEnd);
}
```

### 4. Debug Logging
Enhanced logging tracks timezone handling:
```java
logger.debug("Event times - Start: {}, End: {}", startDateStr, endDateStr);
```

## How It Works

### Example Event Creation

**Input:**
```json
{
  "name": "Tech Conference 2025",
  "description": "Annual technology conference",
  "startDate": "2025-06-15T09:00:00",
  "endDate": "2025-06-16T17:00:00",
  "createdBy": "admin@example.com"
}
```

**Process:**
1. Event times are formatted as: `2025-06-15T09:00:00` and `2025-06-16T17:00:00`
2. These exact strings are sent to the AI
3. AI generates agenda with times in the same format and range
4. System parses times as `LocalDateTime` (timezone-agnostic)
5. Validation ensures all times are within boundaries
6. Agenda items are saved with consistent timezone context

**Generated Agenda (Example):**
```json
[
  {
    "title": "Registration",
    "startTime": "2025-06-15T09:00:00",
    "endTime": "2025-06-15T09:30:00"
  },
  {
    "title": "Opening Keynote",
    "startTime": "2025-06-15T09:30:00",
    "endTime": "2025-06-15T10:30:00"
  },
  {
    "title": "Closing Session",
    "startTime": "2025-06-16T16:00:00",
    "endTime": "2025-06-16T17:00:00"
  }
]
```

## Key Points

### ✅ What We Do
- Use `LocalDateTime` which is timezone-agnostic
- Send event times in ISO 8601 local format to AI
- Explicitly instruct AI to maintain timezone consistency
- Validate all generated times are within event boundaries
- Log warnings if times fall outside boundaries

### ❌ What We Don't Do
- Don't use `ZonedDateTime` or timezone offsets in the prompt
- Don't convert timezones during generation
- Don't allow AI to interpret timezones independently
- Don't fail event creation if validation warnings occur

## Testing Timezone Consistency

### Test Case 1: Single Timezone Event
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Morning Workshop",
    "description": "Half-day workshop",
    "startDate": "2025-05-20T09:00:00",
    "endDate": "2025-05-20T13:00:00",
    "createdBy": "admin@example.com"
  }'
```

**Expected:** All agenda items have times between 09:00:00 and 13:00:00 on 2025-05-20

### Test Case 2: Multi-Day Event
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Conference 2025",
    "description": "Three-day conference",
    "startDate": "2025-09-10T08:00:00",
    "endDate": "2025-09-12T18:00:00",
    "createdBy": "admin@example.com"
  }'
```

**Expected:** 
- Day 1 agenda items: 2025-09-10 between 08:00:00 and 18:00:00
- Day 2 agenda items: 2025-09-11 between 08:00:00 and 18:00:00
- Day 3 agenda items: 2025-09-12 between 08:00:00 and 18:00:00

### Test Case 3: Evening Event
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Networking Evening",
    "description": "Evening networking event",
    "startDate": "2025-07-15T18:00:00",
    "endDate": "2025-07-15T21:00:00",
    "createdBy": "admin@example.com"
  }'
```

**Expected:** All agenda items have times between 18:00:00 and 21:00:00 on 2025-07-15

## Monitoring

### Successful Generation
```
INFO - Creating event: Tech Conference 2025
DEBUG - Event times - Start: 2025-06-15T09:00:00, End: 2025-06-16T17:00:00
INFO - Generating agenda for event: Tech Conference 2025
INFO - Generated 12 agenda items for event
INFO - Event created with ID: 675f3a21e8c9f0001a4b2c3d
```

### Validation Warnings
```
WARN - Agenda item 'Late Session' start time 2025-06-16T18:00:00 is outside event boundaries [2025-06-15T09:00:00, 2025-06-16T17:00:00]
```
This indicates the AI generated a time outside the event range (rare but logged for visibility).

## Best Practices

1. **Always use ISO 8601 local format** when creating events
2. **Check logs** for validation warnings after event creation
3. **Verify agenda times** match your expected timezone
4. **Report issues** if AI consistently generates times outside boundaries
5. **Document timezone** in event description if critical for attendees

## Technical Notes

### Why LocalDateTime?
- Timezone-agnostic representation
- Matches frontend/client expectations (they apply their own timezone)
- Simplifies database storage
- Avoids DST conversion issues
- API consumers handle their own timezone display

### Why Not ZonedDateTime?
- Would require timezone information from clients
- Adds complexity to API contract
- Frontend would need to handle timezone conversion
- Database storage becomes more complex
- Different attendees may be in different timezones

### Current Approach Benefits
- **Simple API**: Clients send and receive local times
- **Flexible**: Each user interprets times in their own timezone
- **Consistent**: No automatic conversions
- **Predictable**: What you send is what you get

## Troubleshooting

### Issue: Agenda times don't match expected timezone
**Solution:** Check the event start/end times you provided. The agenda will match those exact times.

### Issue: Validation warnings about times outside boundaries
**Solution:** This is rare. The AI may have misunderstood. Check logs and contact support if persistent.

### Issue: Times appear different in UI
**Solution:** Your browser/UI may be converting times to your local timezone. This is expected client-side behavior.

## Summary

✅ **Timezone consistency is enforced through:**
1. Explicit AI prompt instructions
2. ISO 8601 local datetime format
3. Runtime validation with logging
4. Timezone-agnostic LocalDateTime usage

✅ **Result:** All agenda items are generated with times in the same timezone context as the event, ensuring consistency and preventing confusion.

