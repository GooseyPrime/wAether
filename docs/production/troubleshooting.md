# wAether Troubleshooting Guide

This guide provides step-by-step troubleshooting procedures for common issues encountered in the wAether application.

## Quick Diagnostic Checklist

Before diving into specific issues, run through this quick checklist:

- [ ] Check device connectivity (WiFi/cellular)
- [ ] Verify app permissions (Location, Storage)
- [ ] Check Firebase Console for service status
- [ ] Review recent app updates or configuration changes
- [ ] Check external API service status pages

## Common Issues and Solutions

### 1. Data Not Updating

#### Symptoms
- Weather data shows stale information
- Space weather metrics not refreshing
- "Last updated" timestamp not changing

#### Investigation Steps

```bash
# Check WorkManager status
adb shell dumpsys jobscheduler | grep wAether

# Review app logs
adb logcat -s wAether:* DataRepository:* GlobalSnapshotWorker:*

# Check network connectivity
adb shell ping -c 3 api.open-meteo.com
adb shell ping -c 3 services.swpc.noaa.gov
```

#### Common Causes and Solutions

**Background Workers Disabled**
```kotlin
// Check if background workers are scheduled
val workManager = WorkManager.getInstance(context)
val workInfos = workManager.getWorkInfosByTag("global_snapshot").get()
if (workInfos.isEmpty()) {
    // Reschedule workers
    SnapshotScheduler.schedulePeriodicGlobalSnapshots(context)
}
```

**API Rate Limiting**
- **OpenMeteo**: Check if daily quota exceeded
- **Solution**: Implement exponential backoff and caching
- **Monitoring**: Review API usage patterns

**Network Connectivity Issues**
- **Check**: Device network settings
- **Solution**: Retry with different network or restart networking
- **Code**: Implement network state monitoring

**Location Permission Denied**
```kotlin
// Check location permissions
if (ContextCompat.checkSelfPermission(context, 
    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    // Request location permission
    ActivityCompat.requestPermissions(activity, 
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 
        LOCATION_PERMISSION_REQUEST_CODE)
}
```

### 2. Firebase Sync Issues

#### Symptoms
- Mood logs not saving
- Data not persisting across app restarts
- Authentication failures

#### Investigation Steps

```bash
# Check Firebase Console
1. Open Firebase Console
2. Check Firestore usage and quotas
3. Review authentication logs
4. Check security rule violations

# App-side debugging
adb logcat -s FirebaseFirestore:* FirebaseAuth:*
```

#### Common Causes and Solutions

**Authentication Failures**
```kotlin
// Re-authenticate user
FirebaseAuth.getInstance().signInAnonymously()
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d(TAG, "Re-authentication successful")
            // Retry failed operations
        } else {
            Log.e(TAG, "Re-authentication failed", task.exception)
        }
    }
```

**Security Rule Violations**
- **Check**: Firestore security rules in Firebase Console
- **Common Issue**: Timestamp validation failures
- **Solution**: Update security rules or fix data format

**Quota Exceeded**
- **Firestore Reads**: Check daily read quotas
- **Firestore Writes**: Monitor write operations
- **Solution**: Implement local caching and batch operations

**Network Connectivity**
```kotlin
// Check Firebase connectivity
val db = Firebase.firestore
db.collection("test").document("connectivity")
    .set(mapOf("timestamp" to FieldValue.serverTimestamp()))
    .addOnSuccessListener {
        Log.d(TAG, "Firebase connectivity OK")
    }
    .addOnFailureListener { e ->
        Log.e(TAG, "Firebase connectivity issue", e)
    }
```

### 3. Battery Drain Issues

#### Symptoms
- Rapid battery consumption
- Device heating up
- User complaints about battery life

#### Investigation Steps

```bash
# Check battery usage
adb shell dumpsys batterystats | grep wAether

# Monitor CPU usage
adb shell top | grep wAether

# Check wake locks
adb shell dumpsys power | grep wAether
```

#### Common Causes and Solutions

**Excessive Background Processing**
```kotlin
// Optimize WorkManager intervals
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresBatteryNotLow(true)
    .build()

val workRequest = PeriodicWorkRequestBuilder<GlobalSnapshotWorker>(
    6, TimeUnit.HOURS // Increase interval if needed
).setConstraints(constraints).build()
```

**Location Services Overuse**
```kotlin
// Optimize location requests
val locationRequest = LocationRequest.create().apply {
    interval = 300000 // 5 minutes instead of continuous
    fastestInterval = 60000 // 1 minute minimum
    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
}
```

**Sensor Polling Frequency**
```kotlin
// Reduce magnetometer polling
sensorManager.registerListener(
    magnetometerListener,
    magnetometer,
    SensorManager.SENSOR_DELAY_NORMAL // Instead of SENSOR_DELAY_FASTEST
)
```

**Wake Lock Issues**
```kotlin
// Ensure wake locks are released
try {
    // Critical work
} finally {
    if (wakeLock.isHeld) {
        wakeLock.release()
    }
}
```

### 4. Watch Face Display Issues

#### Symptoms
- Data not displaying on watch face
- UI elements misaligned
- Rendering performance issues

#### Investigation Steps

```bash
# Check display logs
adb logcat -s WatchFaceRenderer:* WatchFaceViewModel:*

# Check Compose performance
adb logcat -s Choreographer:*

# Memory usage
adb shell dumpsys meminfo com.wAether
```

#### Common Causes and Solutions

**Data Binding Issues**
```kotlin
// Check StateFlow connections
val watchFaceViewModel = ViewModelProvider(this)[WatchFaceViewModel::class.java]

// Debug data flow
watchFaceViewModel.localTemperature.collect { temp ->
    Log.d(TAG, "Temperature updated: $temp")
}
```

**Compose Performance**
```kotlin
// Optimize Compose updates
@Composable
fun TemperatureDisplay(temperature: Float?) {
    // Use remember to avoid recomposition
    val formattedTemp = remember(temperature) {
        temperature?.let { String.format("%.1f°C", it) } ?: "--"
    }
    
    Text(text = formattedTemp)
}
```

**Memory Leaks**
- **Check**: ViewModels properly scoped
- **Solution**: Use appropriate lifecycle-aware components
- **Monitor**: Memory usage patterns

### 5. Permission Issues

#### Symptoms
- Location data unavailable
- Features not working properly
- Permission request loops

#### Investigation Steps

```bash
# Check app permissions
adb shell dumpsys package com.wAether | grep permission

# Check permission state
adb shell pm list permissions -g
```

#### Common Causes and Solutions

**Location Permission Denied**
```kotlin
// Implement proper permission flow
when {
    ContextCompat.checkSelfPermission(context, 
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
        // Permission granted, proceed
    }
    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
        // Show rationale and request permission
        showPermissionRationale()
    }
    else -> {
        // Request permission
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
```

**Background Processing Permission**
```kotlin
// For Android 6.0+ (API level 23)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    intent.data = Uri.parse("package:$packageName")
    startActivity(intent)
}
```

### 6. Performance Issues

#### Symptoms
- Slow app startup
- Laggy UI interactions
- High memory usage

#### Investigation Steps

```bash
# Profile app startup
adb shell am start -W com.wAether/.MainActivity

# Monitor memory usage
adb shell dumpsys meminfo com.wAether

# Check for ANRs
adb logcat -s ActivityManager:*
```

#### Common Causes and Solutions

**Slow Startup**
```kotlin
// Optimize Application.onCreate()
class WAetherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Move heavy initialization to background thread
        CoroutineScope(Dispatchers.IO).launch {
            initializeHeavyComponents()
        }
    }
}
```

**Memory Leaks**
```kotlin
// Proper ViewModel cleanup
class WatchFaceViewModel : ViewModel() {
    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )
    
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
```

**Database Performance**
```kotlin
// Optimize Firestore queries
val query = db.collection("mood_logs")
    .whereEqualTo("userId", userId)
    .orderBy("timestamp", Query.Direction.DESCENDING)
    .limit(10) // Limit results
```

## Debugging Tools and Techniques

### Logging Best Practices

```kotlin
// Use appropriate log levels
Log.v(TAG, "Verbose debug information")
Log.d(TAG, "Debug information")
Log.i(TAG, "General information")
Log.w(TAG, "Warning message")
Log.e(TAG, "Error message", exception)
```

### Network Debugging

```kotlin
// Add logging interceptor
if (BuildConfig.DEBUG) {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    
    okHttpClient.addInterceptor(logging)
}
```

### Performance Profiling

```bash
# Enable GPU profiling
adb shell setprop debug.hwui.profile true

# Monitor frame rate
adb shell dumpsys gfxinfo com.wAether framestats

# Memory profiling
adb shell am start -a android.intent.action.MAIN -n com.wAether/.MainActivity --enable-debugger
```

## Emergency Procedures

### Complete App Reset

```bash
# Clear app data
adb shell pm clear com.wAether

# Reinstall app
adb uninstall com.wAether
adb install app-debug.apk
```

### Force Stop Background Workers

```kotlin
// Cancel all work
WorkManager.getInstance(context).cancelAllWork()

// Reschedule critical workers
SnapshotScheduler.schedulePeriodicGlobalSnapshots(context)
```

### Database Reset

```kotlin
// Clear local cache (if using Room)
database.clearAllTables()

// Reset Firebase sync
FirebaseAuth.getInstance().signOut()
FirebaseAuth.getInstance().signInAnonymously()
```

## Escalation Procedures

### Level 1: Self-Service
- Check this troubleshooting guide
- Review app logs and system diagnostics
- Try basic remediation steps

### Level 2: Developer Support
- **Contact**: Development team
- **Information Needed**: 
  - Device model and OS version
  - App version
  - Steps to reproduce
  - Log files
  - Error screenshots

### Level 3: External Service Issues
- **Firebase**: Firebase Support Console
- **OpenMeteo**: Community forum or paid support
- **NOAA**: Service status pages and documentation

## Preventive Measures

### Regular Health Checks

```kotlin
// Implement health check endpoint
class HealthChecker {
    suspend fun performHealthCheck(): HealthStatus {
        return HealthStatus(
            firebase = checkFirebaseHealth(),
            openMeteo = checkOpenMeteoHealth(),
            noaaSwpc = checkNoaaSwpcHealth(),
            location = checkLocationServices(),
            sensors = checkSensorAvailability()
        )
    }
}
```

### Monitoring and Alerts

- Set up Firebase Performance Monitoring
- Implement crash reporting with detailed context
- Monitor API response times and error rates
- Track battery usage patterns

### User Education

- Provide clear error messages
- Implement graceful degradation
- Offer offline functionality where possible
- Guide users through permission setup

---

**Last Updated**: 2025-09-08T13:45:00-04:00 / 2025-09-08T17:45:00Z — Comprehensive troubleshooting procedures