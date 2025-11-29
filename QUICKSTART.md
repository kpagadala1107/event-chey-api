# Quick Start Guide - Event Chey API

## Prerequisites Check

Before starting, ensure you have:

- ✅ Java 17+ installed
- ✅ Maven 3.6+ installed (or use the included Maven wrapper)
- ✅ MongoDB 4.4+ running

## Step 1: Install and Start MongoDB

### macOS (using Homebrew)
```bash
# Install MongoDB
brew tap mongodb/brew
brew install mongodb-community

# Start MongoDB service
brew services start mongodb-community

# Verify MongoDB is running
mongosh --eval "db.version()"
```

### Linux (Ubuntu/Debian)
```bash
# Import MongoDB public GPG key
wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | sudo apt-key add -

# Create list file
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/6.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-6.0.list

# Install MongoDB
sudo apt-get update
sudo apt-get install -y mongodb-org

# Start MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod

# Verify
mongosh --eval "db.version()"
```

### Windows
Download and install from: https://www.mongodb.com/try/download/community

## Step 2: Initialize MongoDB Database

```bash
# Connect to MongoDB
mongosh

# Create database and indexes
use eventchey;

db.events.createIndex({ "createdBy": 1 });
db.events.createIndex({ "attendees.email": 1 });
db.events.createIndex({ "startDate": 1 });
db.events.createIndex({ "agenda.id": 1 });

# Exit MongoDB shell
exit
```

## Step 3: Build the Application

```bash
cd event-chey-api

# Build with Maven wrapper (recommended)
./mvnw clean install

# Or use system Maven
mvn clean install
```

## Step 4: Run the Application

### Option 1: Using Maven (Development)
```bash
./mvnw spring-boot:run
```

### Option 2: Using JAR (Production)
```bash
java -jar target/event-chey-api-0.0.1-SNAPSHOT.jar
```

### Option 3: With Custom Port
```bash
java -jar target/event-chey-api-0.0.1-SNAPSHOT.jar --server.port=9090
```

## Step 5: Verify Application is Running

### Check Application Health
```bash
# Using curl
curl http://localhost:8080/swagger-ui.html

# Or open in browser
open http://localhost:8080/swagger-ui.html
```

You should see the Swagger UI documentation page.

## Step 6: Test the API

### 1. Create an Event
```bash
curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My First Event",
    "description": "Testing the Event Chey API",
    "startDate": "2025-12-15T10:00:00",
    "endDate": "2025-12-15T17:00:00",
    "createdBy": "test@example.com"
  }'
```

Save the returned `id` field for the next steps.

### 2. Get Event by ID
```bash
curl http://localhost:8080/events/{EVENT_ID}
```

### 3. Invite Attendees
```bash
curl -X POST http://localhost:8080/events/{EVENT_ID}/invite \
  -H "Content-Type: application/json" \
  -d '[
    {
      "email": "attendee1@example.com",
      "phone": "+1234567890",
      "name": "John Attendee"
    }
  ]'
```

### 4. Add Agenda Item
```bash
curl -X POST http://localhost:8080/events/{EVENT_ID}/agenda \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Opening Keynote",
    "startTime": "2025-12-15T10:00:00",
    "endTime": "2025-12-15T11:00:00",
    "description": "Welcome and introduction",
    "speaker": "Jane Speaker"
  }'
```

Save the returned `id` field as `AGENDA_ID`.

### 5. Add a Question
```bash
curl -X POST http://localhost:8080/agenda/{AGENDA_ID}/questions \
  -H "Content-Type: application/json" \
  -d '{
    "attendeeId": "att-001",
    "questionText": "What time is lunch?"
  }'
```

### 6. Create a Poll
```bash
curl -X POST http://localhost:8080/agenda/{AGENDA_ID}/polls \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What topics interest you most?",
    "options": [
      "Microservices",
      "Cloud Native",
      "AI/ML",
      "Security"
    ]
  }'
```

Save the returned `id` field as `POLL_ID`.

### 7. Submit a Vote
```bash
curl -X POST http://localhost:8080/agenda/{AGENDA_ID}/polls/{POLL_ID}/vote \
  -H "Content-Type: application/json" \
  -d '{
    "option": "Microservices"
  }'
```

## Useful Endpoints

| What | URL |
|------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| Application | http://localhost:8080 |

## Troubleshooting

### MongoDB Connection Error
```
Error: Connection refused
```

**Solution:** Ensure MongoDB is running:
```bash
# macOS
brew services list | grep mongodb

# Linux
sudo systemctl status mongod

# Restart if needed
brew services restart mongodb-community  # macOS
sudo systemctl restart mongod            # Linux
```

### Port Already in Use
```
Error: Port 8080 already in use
```

**Solution:** Use a different port:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Java Version Error
```
Error: Java 17 or higher required
```

**Solution:** Check and update Java version:
```bash
java -version

# Install Java 17 (macOS)
brew install openjdk@17

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### MapStruct Compilation Issues
```
Error: Cannot find symbol in mapper
```

**Solution:** Clean and rebuild:
```bash
./mvnw clean compile -U
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Change MongoDB connection
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=eventchey

# Change server port
server.port=8080

# Enable debug logging
logging.level.com.kp.eventchey=DEBUG
```

## Next Steps

1. **Explore API Documentation**: Visit http://localhost:8080/swagger-ui.html
2. **Load Sample Data**: See `MONGODB_SAMPLES.md` for sample data
3. **Test All Endpoints**: Use Postman or cURL with examples from `README.md`
4. **Customize**: Modify domain models and add new features
5. **Deploy**: Package and deploy to cloud platforms

## Production Deployment

### Build Production JAR
```bash
./mvnw clean package -DskipTests
```

### Run in Production Mode
```bash
java -jar target/event-chey-api-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.data.mongodb.uri=mongodb://prod-host:27017/eventchey
```

### Environment Variables
```bash
export SPRING_DATA_MONGODB_URI=mongodb://your-mongo-host:27017/eventchey
export JWT_SECRET=your-production-secret-key
export SERVER_PORT=8080

java -jar target/event-chey-api-0.0.1-SNAPSHOT.jar
```

## Support

For issues and questions:
- Check `README.md` for detailed documentation
- Review `MONGODB_SAMPLES.md` for data examples
- Consult Swagger UI for API reference

---

**🎉 You're all set! Start building amazing event management applications with Event Chey API!**

