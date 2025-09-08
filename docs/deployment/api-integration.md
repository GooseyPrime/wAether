# wAether API Integration Guide

This document provides detailed information about external API integrations used by the wAether application.

## Overview

The wAether app integrates with several external APIs to collect environmental and space weather data:

1. **OpenMeteo API**: Weather data and forecasts
2. **NOAA Space Weather Prediction Center API**: Space weather and solar activity data
3. **Firebase APIs**: Data persistence and authentication
4. **Google Play Services**: Location and device information

## OpenMeteo Weather API

### Overview
- **Base URL**: `https://api.open-meteo.com/v1/`
- **Documentation**: [open-meteo.com/en/docs](https://open-meteo.com/en/docs)
- **Rate Limits**: 10,000 requests/day (free tier)
- **Authentication**: Optional API key for higher limits

### API Endpoints Used

#### Current Weather Data
```http
GET /v1/forecast?latitude={lat}&longitude={lon}&current_weather=true&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation,weathercode,windspeed_10m
```

**Response Format**:
```json
{
  "current_weather": {
    "temperature": 15.3,
    "windspeed": 10.5,
    "weathercode": 3
  },
  "hourly": {
    "temperature_2m": [14.2, 15.3, 16.1],
    "relativehumidity_2m": [65, 68, 70],
    "apparent_temperature": [12.1, 13.4, 14.2],
    "precipitation": [0, 0.1, 0],
    "weathercode": [2, 3, 61],
    "windspeed_10m": [8.5, 10.5, 12.1]
  }
}
```

### Configuration Parameters

| Parameter | Description | wAether Usage |
|-----------|-------------|---------------|
| `latitude` | Location latitude | User's current location |
| `longitude` | Location longitude | User's current location |
| `current_weather` | Include current conditions | Always `true` |
| `hourly` | Hourly forecast parameters | Temperature, humidity, precipitation |
| `daily` | Daily forecast parameters | Not currently used |
| `timezone` | Timezone for timestamps | Auto-detected |

### Error Handling

```kotlin
// OpenMeteoService.kt example
try {
    val response = openMeteoService.getCurrentWeather(lat, lon, params)
    if (response.isSuccessful) {
        val weatherData = response.body()
        // Process weather data
    } else {
        Log.e(TAG, "OpenMeteo API error: ${response.code()}")
        // Handle API error
    }
} catch (e: Exception) {
    Log.e(TAG, "Network error calling OpenMeteo API", e)
    // Handle network error
}
```

### Rate Limiting Strategy

- **Free Tier**: 10,000 requests/day
- **Request Frequency**: Every 5 minutes (288 requests/day per user)
- **Caching**: 5-minute cache to reduce API calls
- **Fallback**: Use cached data when rate limited

## NOAA Space Weather Prediction Center API

### Overview
- **Base URL**: `https://services.swpc.noaa.gov/`
- **Documentation**: [swpc.noaa.gov/products](https://www.swpc.noaa.gov/products)
- **Rate Limits**: No published limits (reasonable use expected)
- **Authentication**: None required

### API Endpoints Used

#### Solar X-ray Flux
```http
GET /json/goes/primary/xrays-6-hour.json
```

**Response Format**:
```json
[
  {
    "time_tag": "2023-09-08T12:00:00Z",
    "satellite": 18,
    "flux": 1.23e-06,
    "observed_flux": 1.23e-06,
    "electron_correction": 0.0,
    "electron_contamination": false,
    "energy": "0.1-0.8nm"
  }
]
```

#### Geomagnetic K-index
```http
GET /json/planetary_k_index_1m.json
```

**Response Format**:
```json
[
  {
    "time_tag": "2023-09-08T12:00:00Z",
    "kp": 3.0,
    "kp_index": "3",
    "a_running": 15,
    "station_count": 13
  }
]
```

#### Solar Wind Data
```http
GET /json/rtsw/rtsw_mag_1m.json
```

**Response Format**:
```json
[
  {
    "time_tag": "2023-09-08T12:00:00Z",
    "bx_gsm": -2.1,
    "by_gsm": 3.4,
    "bz_gsm": -1.8,
    "bt": 4.2,
    "latitude": 12.3,
    "longitude": 45.6
  }
]
```

### Data Processing

```kotlin
// FlareCalculator.kt example
object FlareCalculator {
    fun calculateXrayClass(flux: Double): String {
        return when {
            flux >= 1e-4 -> "X${String.format("%.1f", flux * 10000)}"
            flux >= 1e-5 -> "M${String.format("%.1f", flux * 100000)}"
            flux >= 1e-6 -> "C${String.format("%.1f", flux * 1000000)}"
            flux >= 1e-7 -> "B${String.format("%.1f", flux * 10000000)}"
            else -> "A${String.format("%.1f", flux * 100000000)}"
        }
    }
}
```

## Firebase APIs

### Firestore Database

#### Collections Used
- `mood_logs`: User mood entries with environmental data
- `global_snapshots`: System-wide environmental snapshots

#### Security Configuration
```javascript
// Firestore security rules
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /mood_logs/{document} {
      allow read, write: if request.auth != null 
        && resource.data.userId == request.auth.uid;
    }
  }
}
```

### Firebase Authentication

Currently configured for anonymous authentication:

```kotlin
FirebaseAuth.getInstance().signInAnonymously()
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val user = task.result?.user
            Log.d(TAG, "Anonymous sign-in successful: ${user?.uid}")
        } else {
            Log.e(TAG, "Anonymous sign-in failed", task.exception)
        }
    }
```

## Google Play Services

### Location Services

```kotlin
// LocationProvider.kt
private val fusedLocationClient: FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(context)

fun getCurrentLocation(): Flow<Location?> = flow {
    try {
        val location = fusedLocationClient.lastLocation.await()
        emit(location)
    } catch (e: SecurityException) {
        Log.e(TAG, "Location permission denied", e)
        emit(null)
    }
}
```

### Required Permissions
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## API Key Management

### Environment Configuration

```bash
# .env file
OPENMETEO_API_KEY=optional-for-basic-usage
FIREBASE_API_KEY=your-firebase-web-api-key
GOOGLE_SERVICES_API_KEY=configured-in-google-services.json
```

### Security Best Practices

1. **API Key Rotation**: Regularly rotate API keys
2. **Rate Limiting**: Implement client-side rate limiting
3. **Error Handling**: Graceful degradation when APIs unavailable
4. **Caching**: Cache responses to reduce API calls
5. **Monitoring**: Monitor API usage and error rates

## Monitoring and Alerts

### API Health Monitoring

```kotlin
// ApiHealthChecker.kt
class ApiHealthChecker {
    suspend fun checkOpenMeteoHealth(): Boolean {
        return try {
            val response = openMeteoService.getCurrentWeather(0.0, 0.0, "current_weather=true")
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun checkNoaaSwpcHealth(): Boolean {
        return try {
            val response = noaaSwpcService.getXrayFlux()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
```

### Performance Metrics

Track these metrics for each API:
- Response time (95th percentile)
- Success rate
- Rate limit usage
- Error distribution
- Data freshness

## Troubleshooting

### Common Issues

#### OpenMeteo API Issues
- **Rate Limiting**: Implement exponential backoff
- **Location Errors**: Validate GPS coordinates
- **Network Timeouts**: Increase timeout values
- **Invalid Parameters**: Validate request parameters

#### NOAA SWPC API Issues
- **Service Outages**: Implement fallback data sources
- **Data Delays**: Handle stale data scenarios
- **Format Changes**: Monitor for API schema changes

#### Firebase Issues
- **Authentication Failures**: Implement retry logic
- **Quota Exceeded**: Monitor usage and implement caching
- **Security Rule Violations**: Validate data before writing

### Error Recovery Strategies

```kotlin
// RetryableApiCall.kt
suspend fun <T> retryableApiCall(
    maxRetries: Int = 3,
    delayMs: Long = 1000,
    apiCall: suspend () -> T
): Result<T> {
    repeat(maxRetries) { attempt ->
        try {
            return Result.success(apiCall())
        } catch (e: Exception) {
            if (attempt == maxRetries - 1) {
                return Result.failure(e)
            }
            delay(delayMs * (attempt + 1)) // Exponential backoff
        }
    }
    return Result.failure(Exception("Max retries exceeded"))
}
```

## API Documentation Updates

This document should be updated when:
- New APIs are integrated
- Existing API endpoints change
- Rate limits are modified
- Authentication methods change
- Error handling strategies are updated

---

**Last Updated**: 2025-09-08T13:40:00-04:00 / 2025-09-08T17:40:00Z — Comprehensive API integration documentation