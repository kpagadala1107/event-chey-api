# 🔧 CORS Troubleshooting Guide - UPDATED

## ✅ What Was Fixed

### 1. **Changed `setAllowedOrigins` to `setAllowedOriginPatterns`**
   - When `setAllowCredentials(true)`, Spring Security requires `setAllowedOriginPatterns`
   - This is a Spring Security requirement to prevent credential leaks

### 2. **Added @CrossOrigin Annotations**
   - Added `@CrossOrigin` to all 4 controllers as a backup layer
   - Provides controller-level CORS support

### 3. **Enhanced Configuration**
   - Added "HEAD" method support
   - Added "Location" to exposed headers
   - Added wildcard pattern `http://localhost:*` for any port

## 🧪 Testing CORS

### Method 1: Use the Test HTML File

I've created a test file: `cors-test.html`

**To test:**
```bash
# Option A: Serve from localhost:3000
cd /Users/kiranpagadala/IdeaProjects/event-chey-api
npx http-server -p 3000

# Option B: Simple Python server
python3 -m http.server 3000

# Then open: http://localhost:3000/cors-test.html
```

### Method 2: Test from Browser Console

Open your React app at `http://localhost:3000` and run in the console:

```javascript
// Test GET
fetch('http://localhost:8080/events', {
  method: 'GET',
  credentials: 'include'
})
.then(r => r.json())
.then(d => console.log('Success:', d))
.catch(e => console.error('Error:', e));

// Test POST
fetch('http://localhost:8080/events', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  credentials: 'include',
  body: JSON.stringify({
    name: 'Test Event',
    description: 'Testing CORS',
    startDate: '2025-12-01T09:00:00',
    endDate: '2025-12-01T17:00:00',
    createdBy: 'test@example.com'
  })
})
.then(r => r.json())
.then(d => console.log('Created:', d))
.catch(e => console.error('Error:', e));
```

### Method 3: cURL with CORS Headers

```bash
# Test OPTIONS (preflight)
curl -i -X OPTIONS http://localhost:8080/events \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type"

# You should see:
# Access-Control-Allow-Origin: http://localhost:3000
# Access-Control-Allow-Credentials: true
# Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD
```

## 🔍 Common CORS Errors & Solutions

### Error 1: "No 'Access-Control-Allow-Origin' header"
```
Access to fetch at 'http://localhost:8080/events' from origin 'http://localhost:3000' 
has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present
```

**Solutions:**
1. ✅ Make sure backend is running: `./mvnw spring-boot:run`
2. ✅ Restart backend after making changes
3. ✅ Check frontend is on port 3000
4. ✅ Clear browser cache (Cmd+Shift+R / Ctrl+Shift+R)

### Error 2: "Credentials flag is true but Access-Control-Allow-Credentials is not 'true'"
```
The value of the 'Access-Control-Allow-Credentials' header in the response is '' 
which must be 'true' when the request's credentials mode is 'include'
```

**Solution:**
✅ Already fixed! We use `setAllowedOriginPatterns` now instead of `setAllowedOrigins`

### Error 3: "The wildcard '*' cannot be used when credentials flag is true"
```
The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' 
when the request's credentials mode is 'include'
```

**Solution:**
✅ Already fixed! We specify exact origins instead of "*"

### Error 4: Preflight Request Fails (OPTIONS)
```
Response to preflight request doesn't pass access control check
```

**Solutions:**
1. ✅ Added OPTIONS to allowed methods
2. ✅ Added @CrossOrigin to controllers
3. ✅ SecurityConfig permits all requests to `/events/**`

## 🚀 Restart Your Backend

**Important:** After these changes, you MUST restart the backend:

```bash
# Stop current instance (Ctrl+C)

# Rebuild and run
cd /Users/kiranpagadala/IdeaProjects/event-chey-api
./mvnw spring-boot:run
```

## 📝 Current CORS Configuration

### SecurityConfig.java
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // Use patterns when credentials are enabled
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:3000",
        "http://127.0.0.1:3000",
        "http://localhost:*"  // Any localhost port
    ));
    
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
    ));
    
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setExposedHeaders(Arrays.asList(
        "Authorization", "Content-Type", "X-Total-Count", "Location"
    ));
    configuration.setMaxAge(3600L);
    
    // Apply to all endpoints
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### All Controllers Have:
```java
@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
    allowCredentials = "true",
    allowedHeaders = "*",
    methods = {GET, POST, PUT, DELETE, OPTIONS, PATCH}
)
```

## 🔧 Still Not Working?

### Step 1: Verify Backend Is Running
```bash
curl http://localhost:8080/events
# Should return [] or list of events
```

### Step 2: Check CORS Headers
```bash
curl -i -H "Origin: http://localhost:3000" http://localhost:8080/events
# Look for: Access-Control-Allow-Origin: http://localhost:3000
```

### Step 3: Check Browser Console
- Open DevTools (F12)
- Go to Network tab
- Look at the request headers and response headers
- Check if preflight (OPTIONS) request succeeded

### Step 4: Verify Frontend URL
- Must be exactly `http://localhost:3000` or `http://127.0.0.1:3000`
- Not `localhost:3000` (missing protocol)
- Not `http://localhost:3001` (wrong port)

### Step 5: Clear Everything
```bash
# Clear browser cache
# Hard refresh: Cmd+Shift+R (Mac) or Ctrl+Shift+R (Windows)

# Rebuild backend
./mvnw clean install
./mvnw spring-boot:run
```

## 🎯 Checklist

Before reporting CORS still doesn't work:

- [ ] Backend is running on port 8080
- [ ] Frontend is running on port 3000
- [ ] Backend was restarted after changes
- [ ] Browser cache was cleared
- [ ] Tested in browser console (see Method 2 above)
- [ ] Checked browser console for exact error message
- [ ] Verified request includes `credentials: 'include'` or `withCredentials: true`
- [ ] OPTIONS preflight request succeeds (check Network tab)

## 📱 From Your React App

### Axios Configuration
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Use it
api.get('/events')
  .then(response => console.log(response.data))
  .catch(error => console.error(error));

api.post('/events', eventData)
  .then(response => console.log(response.data))
  .catch(error => console.error(error));
```

### Fetch Configuration
```javascript
const api = {
  baseURL: 'http://localhost:8080',
  
  async get(endpoint) {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.json();
  },
  
  async post(endpoint, data) {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return response.json();
  }
};

// Use it
api.get('/events').then(console.log).catch(console.error);
api.post('/events', eventData).then(console.log).catch(console.error);
```

## 🎉 Expected Behavior

When CORS is working correctly:

1. **OPTIONS** request succeeds with 200 status
2. **Actual** request (GET/POST/etc) succeeds
3. **No CORS errors** in browser console
4. **Response headers include:**
   - `Access-Control-Allow-Origin: http://localhost:3000`
   - `Access-Control-Allow-Credentials: true`
   - `Access-Control-Allow-Methods: ...`

## 📞 Need More Help?

If CORS still doesn't work after following this guide:

1. Check the exact error message in browser console
2. Check the Network tab for request/response headers
3. Verify both backend and frontend are running on correct ports
4. Try the test HTML file (`cors-test.html`)
5. Share the exact error message and screenshots

---

**Status:** ✅ CORS Configuration Complete
**Build:** ✅ SUCCESS
**Ready:** ✅ YES

**Action Required:** Restart your backend and test!

