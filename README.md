# wAether ⌚

[![CI/CD Pipeline](https://github.com/GooseyPrime/wAether/actions/workflows/ci.yml/badge.svg)](https://github.com/GooseyPrime/wAether/actions/workflows/ci.yml)

A sophisticated Wear OS watch face that displays weather data, space weather information, and local sensor readings to help you understand the environment around you.

## Features

- **Weather Integration**: Local weather conditions via OpenMeteo API
- **Space Weather**: Solar activity, X-ray flux, and Kp index data from NOAA
- **Device Sensors**: Magnetometer readings for local magnetic field data
- **Firebase Integration**: Data logging and mood tracking
- **Material Design**: Modern Wear OS UI with Jetpack Compose
- **Background Processing**: Periodic data collection with WorkManager

## Architecture

This project follows Android architecture best practices:

- **MVVM Pattern**: Clear separation between UI, business logic, and data
- **Repository Pattern**: Centralized data access layer
- **Dependency Injection**: Clean dependency management
- **Coroutines**: Asynchronous operations and reactive programming
- **Jetpack Compose**: Modern declarative UI for Wear OS

## Development

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Wear OS emulator or physical Wear OS device
- Firebase project setup (optional for full functionality)

### Building

```bash
# Clone the repository
git clone https://github.com/GooseyPrime/wAether.git
cd wAether

# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Run linting
./gradlew ktlintCheck
```

### Testing

The project includes both unit tests and instrumented tests:

- **Unit Tests**: `app/src/test/java/com/wAether/`
- **Instrumented Tests**: `app/src/androidTest/java/com/wAether/`

### Code Quality

We maintain high code quality through:

- **Kotlin Linting**: Automated with ktlint
- **Android Lint**: Static analysis for Android-specific issues  
- **Unit Testing**: Comprehensive test coverage
- **Instrumented Testing**: UI and integration tests
- **CI/CD Pipeline**: Automated testing and deployment

## CI/CD

Our GitHub Actions workflow ensures code quality and reliability:

- 🔍 **Linting**: Kotlin and Android lint checks
- 🧪 **Testing**: Unit and instrumented tests
- 🔨 **Building**: Debug and release APK generation
- 🚀 **Deployment**: Automated releases for tagged commits

## APIs Used

- [OpenMeteo](https://open-meteo.com/): Weather data
- [NOAA SWPC](https://services.swpc.noaa.gov/): Space weather data
- [Firebase Firestore](https://firebase.google.com/docs/firestore): Data storage
- [Google Play Services](https://developers.google.com/android/guides/overview): Location services

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please ensure your code follows our style guidelines and passes all tests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Space weather data provided by NOAA Space Weather Prediction Center
- Weather data provided by Open-Meteo
- Built with ❤️ for the Wear OS community