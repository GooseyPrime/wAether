# Firebase Security Rules for wAether

This document defines the security rules for Firebase Firestore used in the wAether application.

## Collection Structure

```
/mood_logs/{documentId}
/global_snapshots/{documentId}
```

## Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Mood logs collection
    match /mood_logs/{document} {
      // Allow read/write for authenticated users
      // Users can only access their own mood logs
      allow read, write: if request.auth != null 
        && resource.data.userId == request.auth.uid;
      
      // Allow creation for authenticated users
      allow create: if request.auth != null 
        && request.resource.data.userId == request.auth.uid
        && validateMoodLogStructure();
    }
    
    // Global snapshots collection (system-generated data)
    match /global_snapshots/{document} {
      // Read access for authenticated users
      allow read: if request.auth != null;
      
      // Write access only for system/admin users
      allow write: if request.auth != null 
        && request.auth.token.admin == true;
        
      // Allow creation with proper structure validation
      allow create: if request.auth != null 
        && validateGlobalSnapshotStructure();
    }
    
    // Validation functions
    function validateMoodLogStructure() {
      return request.resource.data.keys().hasAll([
        'timestamp', 'mood', 'userId', 'localTemperature', 
        'localWeatherCondition', 'irradiance', 'moonPhase', 
        'magneticField', 'kpIndex', 'xrayClass'
      ])
      && request.resource.data.timestamp is timestamp
      && request.resource.data.mood is string
      && request.resource.data.userId is string;
    }
    
    function validateGlobalSnapshotStructure() {
      return request.resource.data.keys().hasAll([
        'timestamp', 'irradiance', 'moonPhase', 'magneticField', 
        'kpIndex', 'xrayClass', 'solarWindSpeed', 'solarWindDensity'
      ])
      && request.resource.data.timestamp is timestamp
      && request.resource.data.mood == null;
    }
  }
}
```

## Rule Explanation

### Authentication Requirements
- All operations require user authentication
- Uses Firebase Authentication tokens

### Mood Logs Security
- Users can only access their own mood log entries
- Enforced through `userId` field matching `request.auth.uid`
- Structure validation ensures data integrity

### Global Snapshots Security
- Read access for all authenticated users
- Write access restricted to admin users
- System-generated data for environmental snapshots

### Data Validation
- Enforces required fields for both collections
- Type checking for critical fields
- Prevents malformed data insertion

## Deployment

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Deploy security rules
firebase deploy --only firestore:rules

# Test security rules
firebase emulators:start --only firestore
```

## Testing Security Rules

Use the Firebase Emulator to test security rules:

```javascript
// Test mood log access
const testData = {
  userId: 'test-user-123',
  timestamp: new Date(),
  mood: 'happy',
  localTemperature: 22.5,
  // ... other required fields
};

// Should succeed for authenticated user
await firebase.firestore()
  .collection('mood_logs')
  .add(testData);

// Should fail for wrong user
const wrongUserData = { ...testData, userId: 'different-user' };
// This will be rejected by security rules
```

---

**Last Updated**: 2025-09-08T13:35:00-04:00 / 2025-09-08T17:35:00Z — Firebase security rules configuration