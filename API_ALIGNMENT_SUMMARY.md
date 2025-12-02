# API Endpoint Updates - Frontend Alignment

## Summary

Updated the backend API endpoints to match the frontend `attendeeApi` structure. The changes ensure the frontend can properly interact with attendee management features.

---

## 🔄 Changes Made

### 1. New Service Methods (EventService.java)

Added three new methods to the `EventService` interface:

```java
List<AttendeeResponse> getAttendees(String eventId);
EventResponse removeAttendee(String eventId, String attendeeId);
EventResponse updateAttendeeStatus(String eventId, String attendeeId, AttendeeStatus status);
```

### 2. Service Implementation (EventServiceImpl.java)

Implemented the three new methods:

#### `getAttendees(String eventId)`
- Retrieves all attendees for a specific event
- Returns a list of `AttendeeResponse` objects
- Returns empty list if no attendees exist

#### `removeAttendee(String eventId, String attendeeId)`
- Removes a specific attendee from an event
- Updates the event's `updatedAt` timestamp
- Throws `ResourceNotFoundException` if attendee not found

#### `updateAttendeeStatus(String eventId, String attendeeId, AttendeeStatus status)`
- Updates the status of a specific attendee
- Updates the event's `updatedAt` timestamp
- Throws `ResourceNotFoundException` if attendee not found

### 3. New Request DTOs

#### `InviteAttendeesRequest.java` (New)
```java
public record InviteAttendeesRequest(
    @NotNull(message = "Attendees list is required")
    @Valid
    List<InviteAttendeeRequest> attendees
)
```
- Wraps the list of attendees for the invite endpoint
- Matches frontend request structure: `{ attendees: [...] }`

#### `UpdateAttendeeStatusRequest.java` (New)
```java
public record UpdateAttendeeStatusRequest(
    @NotNull(message = "Status is required")
    AttendeeStatus status
)
```
- Request body for updating attendee status
- Matches frontend request structure: `{ status: "..." }`

### 4. Updated Controller Endpoints (EventController.java)

#### New Endpoints:

**GET /api/events/{eventId}/attendees**
- Get all attendees for an event
- Returns: `List<AttendeeResponse>`
- Matches: `attendeeApi.getAttendees(eventId)`

**POST /api/events/{eventId}/attendees/invite**
- Invite attendees to an event (new path)
- Request body: `{ "attendees": [...] }`
- Returns: `EventResponse`
- Matches: `attendeeApi.inviteAttendees(eventId, attendees)`

**DELETE /api/events/{eventId}/attendees/{attendeeId}**
- Remove an attendee from an event
- Returns: `EventResponse`
- Matches: `attendeeApi.removeAttendee(eventId, attendeeId)`

**PATCH /api/events/{eventId}/attendees/{attendeeId}**
- Update attendee status
- Request body: `{ "status": "ACCEPTED" }`
- Returns: `EventResponse`
- Matches: `attendeeApi.updateAttendeeStatus(eventId, attendeeId, status)`

#### Legacy Endpoint (Deprecated):

**POST /api/events/{id}/invite** (Deprecated)
- Kept for backward compatibility
- Old endpoint for inviting attendees
- Direct list of attendees without wrapper

---

## 📋 Frontend-Backend Mapping

| Frontend API Call | Backend Endpoint | Method | Request Body |
|------------------|------------------|--------|--------------|
| `getAttendees(eventId)` | `/events/{eventId}/attendees` | GET | - |
| `inviteAttendees(eventId, attendees)` | `/events/{eventId}/attendees/invite` | POST | `{ attendees: [...] }` |
| `removeAttendee(eventId, attendeeId)` | `/events/{eventId}/attendees/{attendeeId}` | DELETE | - |
| `updateAttendeeStatus(eventId, attendeeId, status)` | `/events/{eventId}/attendees/{attendeeId}` | PATCH | `{ status: "..." }` |

---

## 📝 API Request Examples

### Get Attendees
```bash
GET /api/events/event123/attendees

Response:
[
  {
    "id": "att-001",
    "email": "john@example.com",
    "phone": "+1234567890",
    "name": "John Doe",
    "status": "ACCEPTED"
  }
]
```

### Invite Attendees
```bash
POST /api/events/event123/attendees/invite
Content-Type: application/json

{
  "attendees": [
    {
      "email": "jane@example.com",
      "name": "Jane Smith",
      "phone": "+1234567891"
    }
  ]
}

Response: EventResponse (full event with all attendees)
```

### Remove Attendee
```bash
DELETE /api/events/event123/attendees/att-001

Response: EventResponse (full event with updated attendees list)
```

### Update Attendee Status
```bash
PATCH /api/events/event123/attendees/att-001
Content-Type: application/json

{
  "status": "ACCEPTED"
}

Response: EventResponse (full event with updated attendee status)
```

---

## 🔒 Validation

All new endpoints include:
- ✅ Path variable validation
- ✅ Request body validation with `@Valid`
- ✅ Proper error handling with `ResourceNotFoundException`
- ✅ Logging for debugging and monitoring

---

## 🎯 Key Features

### Consistent Response Structure
- All mutating operations return the full `EventResponse`
- Frontend can update its state with the latest event data
- No need for additional GET requests after mutations

### Proper REST Semantics
- GET for retrieval
- POST for creation/invitations
- DELETE for removal
- PATCH for partial updates

### Backward Compatibility
- Legacy `/invite` endpoint maintained as deprecated
- Existing integrations won't break
- Can be removed in future versions

### Email Integration
- Email invitations still work seamlessly
- Only new attendees receive emails
- Duplicate prevention still active

---

## ✅ Validation & Testing

### Compilation Status
All code compiles successfully with no errors.

### Type Safety
- Using Java records for immutable DTOs
- Strong typing for all parameters
- Validation annotations on all inputs

### Error Handling
- Proper exceptions for missing resources
- Validation errors returned with 400 status
- Consistent error response format

---

## 🚀 Frontend Integration

The frontend can now use the attendee API as follows:

```javascript
import attendeeApi from './attendeeApi';

// Get attendees
const attendees = await attendeeApi.getAttendees(eventId);

// Invite new attendees
const updatedEvent = await attendeeApi.inviteAttendees(eventId, [
  { email: "test@example.com", name: "Test User", phone: "+1234567890" }
]);

// Remove attendee
const updatedEvent = await attendeeApi.removeAttendee(eventId, attendeeId);

// Update status
const updatedEvent = await attendeeApi.updateAttendeeStatus(
  eventId, 
  attendeeId, 
  "ACCEPTED"
);
```

---

## 📁 Files Modified/Created

### Modified:
1. ✅ `EventService.java` - Added 3 new methods
2. ✅ `EventServiceImpl.java` - Implemented 3 new methods
3. ✅ `EventController.java` - Added 4 new endpoints, deprecated 1

### Created:
1. ✅ `InviteAttendeesRequest.java` - Request wrapper for invitations
2. ✅ `UpdateAttendeeStatusRequest.java` - Request for status updates

---

## 🎉 Summary

All backend endpoints now perfectly match the frontend API structure. The changes maintain backward compatibility while providing a more RESTful and consistent API design.

**Status: ✅ Complete and Ready for Integration**

