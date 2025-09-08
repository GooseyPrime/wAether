# wAether

A comprehensive Wear OS watch face application that combines local weather data with space weather monitoring and personal mood tracking. wAether provides real-time insights into both terrestrial and cosmic environmental conditions while allowing users to correlate their emotional state with environmental factors.

## 🌟 Features

- **Custom Wear OS Watch Face**: Beautiful, information-rich watch face displaying time, weather, and space weather data
- **Local Weather Integration**: Real-time weather data including temperature, humidity, wind speed, and precipitation
- **Space Weather Monitoring**: Track solar activity including X-ray flux, magnetic field measurements, and Kp index
- **Mood Logging**: Log and correlate your emotional state with environmental conditions
- **Background Data Collection**: Automated data gathering using WorkManager for consistent monitoring
- **Firebase Integration**: Secure cloud storage for all collected data and mood logs
- **Sensor Integration**: Utilizes device magnetometer and location services for enhanced data accuracy

## 🛠️ Technology Stack

- **Language**: Kotlin
- **Platform**: Android Wear OS (API 30+)
- **UI Framework**: Jetpack Compose for Wear OS
- **Architecture**: MVVM with Repository pattern
- **Networking**: Retrofit with OkHttp
- **Database**: Firebase Firestore
- **Background Tasks**: WorkManager
- **Dependency Injection**: Manual DI (ready for Hilt migration)
- **Async Operations**: Kotlin Coroutines

## 📋 Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- Android SDK with Wear OS support
- Java 8 or higher
- Wear OS device or emulator for testing
- Firebase project setup (see Setup section)

## 🚀 Setup & Installation

### 1. Clone the Repository

```bash
git clone https://github.com/GooseyPrime/wAether.git
cd wAether
```

### 2. Firebase Configuration

1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Enable Firestore Database
3. Download `google-services.json` from your Firebase project settings
4. Place `google-services.json` in the `app/` directory
5. Configure Firestore security rules (see [Firebase Setup](#firebase-setup) section)

### 3. API Configuration

The app uses the following external APIs:
- **OpenMeteo API**: For weather data (no API key required)
- **NOAA Space Weather Prediction Center**: For space weather data (no API key required)

### 4. Build the Project

```bash
# Make gradlew executable (if needed)
chmod +x gradlew

# Build debug version
./gradlew assembleDebug

# Build release version
./gradlew assembleRelease
```

## 🧪 Building & Testing

### Build Commands

```bash
# Clean build
./gradlew clean

# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

### Testing Commands

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run all tests
./gradlew check

# Generate test coverage report
./gradlew jacocoTestReport
```

### Linting & Code Quality

```bash
# Run Android lint
./gradlew lint

# Run Kotlin lint (if ktlint is configured)
./gradlew ktlintCheck

# Fix Kotlin lint issues
./gradlew ktlintFormat
```

## 📱 Usage

### Installing on Wear OS Device

1. Enable Developer Options on your Wear OS device
2. Connect via ADB or WiFi debugging
3. Install the app: `./gradlew installDebug`
4. Launch the wAether app from the app drawer
5. Grant required permissions (Location, Body Sensors)
6. Select wAether from the watch face picker

### First Run Setup

1. **Permissions**: The app will request location and sensor permissions
2. **Location Access**: Required for local weather data
3. **Body Sensors**: Used for device sensor integration
4. **Background Processing**: App will set up automatic data collection

### Using the Watch Face

- **Time Display**: Shows current time with customizable styling
- **Weather Data**: Local temperature, conditions, and forecast
- **Space Weather**: X-ray flux levels, magnetic field strength, Kp index
- **Mood Logging**: Tap designated area to log current mood state
- **Data Sync**: Background synchronization with Firebase

## 📁 Project Structure

```
wAether/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/wAether/
│   │   │   │   ├── MainActivity.kt              # Main activity & permissions
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/                   # Data models
│   │   │   │   │   ├── network/                 # API services
│   │   │   │   │   ├── firebase/                # Firebase utilities
│   │   │   │   │   └── repository/              # Data repository layer
│   │   │   │   ├── sensor/                      # Device sensors integration
│   │   │   │   ├── service/                     # Watch face service
│   │   │   │   ├── ui/
│   │   │   │   │   ├── mood/                    # Mood logging UI
│   │   │   │   │   ├── watchface/               # Watch face components
│   │   │   │   │   └── theme/                   # UI theming
│   │   │   │   ├── util/                        # Utility classes
│   │   │   │   └── workers/                     # Background workers
│   │   │   ├── res/                             # Android resources
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                                # Unit tests
│   │   └── androidTest/                         # Instrumented tests
│   ├── build.gradle.kts                         # App build configuration
│   └── google-services.json                     # Firebase config (not in repo)
├── build.gradle.kts                             # Project build configuration
├── settings.gradle.kts                          # Gradle settings
└── gradlew                                      # Gradle wrapper
```

## 🔧 Configuration

### Firebase Setup

1. **Firestore Collections**:
   - `mood_logs`: User mood entries with environmental correlations
   - `global_snapshots`: Automated environmental data snapshots

2. **Security Rules Example**:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /mood_logs/{document} {
      allow read, write: if request.auth != null;
    }
    match /global_snapshots/{document} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

### App Configuration

Key configuration files:
- `app/build.gradle.kts`: Dependencies and build settings
- `AndroidManifest.xml`: Permissions and service declarations
- `app/src/main/res/values/strings.xml`: App strings and labels

## 🤝 Contributing

We welcome contributions to wAether! Please follow these guidelines:

### Getting Started

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes following the project's coding standards
4. Write or update tests as needed
5. Ensure all tests pass: `./gradlew check`
6. Commit your changes: `git commit -m 'Add amazing feature'`
7. Push to your branch: `git push origin feature/amazing-feature`
8. Open a Pull Request

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add documentation for public APIs
- Maintain consistent formatting (consider using ktlint)

### Pull Request Guidelines

- Keep PRs focused and small
- Include descriptive commit messages
- Update documentation if needed
- Add tests for new functionality
- Ensure CI passes (when configured)

### Reporting Issues

When reporting bugs or requesting features:
- Use clear, descriptive titles
- Provide steps to reproduce (for bugs)
- Include device information and logs when relevant
- Check existing issues before creating new ones

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [OpenMeteo](https://open-meteo.com/) for weather data API
- [NOAA Space Weather Prediction Center](https://www.swpc.noaa.gov/) for space weather data
- [Jetpack Compose for Wear OS](https://developer.android.com/training/wearables/compose) for UI framework
- [Firebase](https://firebase.google.com/) for backend services

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/GooseyPrime/wAether/issues)
- **Discussions**: [GitHub Discussions](https://github.com/GooseyPrime/wAether/discussions)
- **Documentation**: [Wiki](https://github.com/GooseyPrime/wAether/wiki)

## 🗺️ Roadmap

- [ ] Enhanced data visualization
- [ ] Additional mood categories
- [ ] Historical data analysis
- [ ] Wear OS Tiles integration
- [ ] Complications for other watch faces
- [ ] Machine learning insights
- [ ] Multi-language support

---

## 📝 Change Log

- `Updated: 2025-01-08T12:00:00-05:00 / 2025-01-08T17:00:00Z — Initial comprehensive README creation`

---

*Made with ❤️ for the Wear OS community*