# CORS Production Fix

## Issue
The production frontend at `https://event-chey.netlify.app` was blocked by CORS policy when trying to access the API at `https://event-chey-api-production.up.railway.app/api/events`.

**Error Message:**
```
Access to XMLHttpRequest at 'https://event-chey-api-production.up.railway.app/api/events' from origin 'https://event-chey.netlify.app' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

## Root Cause
The CORS configuration in `SecurityConfig.java` only allowed localhost origins for development:
- `http://localhost:3000`
- `http://127.0.0.1:3000`
- `http://localhost:*`

The production origin `https://event-chey.netlify.app` was not included in the allowed origins list.

## Solution
Added the production frontend origin to the allowed origins list in `SecurityConfig.java`.

### Changed File
**`src/main/java/com/kp/eventchey/config/SecurityConfig.java`**

Updated the `corsConfigurationSource()` method to include:
```java
configuration.setAllowedOriginPatterns(Arrays.asList(
    "http://localhost:3000",
    "http://127.0.0.1:3000",
    "http://localhost:*",
    "https://event-chey.netlify.app"  // Added production origin
));
```

## CORS Configuration Details
The current CORS configuration now supports:

### Allowed Origins
- `http://localhost:3000` - Local development
- `http://127.0.0.1:3000` - Local development (alternative)
- `http://localhost:*` - Any local port
- `https://event-chey.netlify.app` - Production frontend

### Allowed Methods
- GET
- POST
- PUT
- DELETE
- OPTIONS
- PATCH
- HEAD

### Allowed Headers
- All headers (`*`)

### Other Settings
- **Credentials:** Enabled (`allowCredentials: true`)
- **Exposed Headers:** Authorization, Content-Type, X-Total-Count, Location
- **Max Age:** 3600 seconds (1 hour)

## Next Steps
1. **Deploy the updated code** to Railway:
   ```bash
   git add .
   git commit -m "Fix: Add production origin to CORS configuration"
   git push origin main
   ```

2. **Verify the fix** once deployed:
   - Visit `https://event-chey.netlify.app`
   - Open browser DevTools (F12) → Network tab
   - Perform an action that triggers an API call to `/api/events`
   - Check that the response includes the header:
     ```
     Access-Control-Allow-Origin: https://event-chey.netlify.app
     ```

3. **Test preflight requests** (OPTIONS):
   ```bash
   curl -X OPTIONS https://event-chey-api-production.up.railway.app/api/events \
     -H "Origin: https://event-chey.netlify.app" \
     -H "Access-Control-Request-Method: GET" \
     -v
   ```

## Verification
✅ Code compiles successfully
✅ CORS configuration updated
✅ Production origin added

## Additional Notes
- If you need to add more frontend origins in the future (e.g., staging, preview deployments), add them to the `setAllowedOriginPatterns()` list
- For Netlify deploy previews, you might want to add a pattern like `https://deploy-preview-*.event-chey.netlify.app`
- The configuration uses `setAllowedOriginPatterns()` instead of `setAllowedOrigins()` to support wildcards

## Date
December 4, 2025

