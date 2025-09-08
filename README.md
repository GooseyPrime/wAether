# wAether

A comprehensive Android Wear OS application that collects environmental data, space weather information, and allows users to log mood data while correlating it with environmental conditions.

## Features

- **Real-time Environmental Data**: Weather conditions, temperature, humidity, and precipitation
- **Space Weather Monitoring**: Solar X-ray flux, geomagnetic activity (K-index), and solar wind data
- **Mood Logging**: Correlate personal mood with environmental conditions
- **Sensor Integration**: Local magnetometer readings and device information
- **Firebase Sync**: Secure cloud storage for mood logs and global environmental snapshots
- **Background Processing**: Automated data collection every 5 minutes

## Quick Start

### Prerequisites
- Android Studio Hedgehog 2023.1.1 or later
- JDK 17 or later
- Android SDK with API 30+ (Android 11+) for Wear OS
- Firebase project for data storage

### Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/GooseyPrime/wAether.git
   cd wAether
   ```

2. **Configure environment**:
   ```bash
   cp .env.template .env
   # Edit .env with your configuration (see README_ENV.md for details)
   ```

3. **Set up Firebase**:
   - Create a Firebase project
   - Download `google-services.json` and place in `app/` directory
   - Configure Firestore database

4. **Build and run**:
   ```bash
   chmod +x ./gradlew
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

## Documentation

### 📚 Complete Documentation
- **[Environment Setup](README_ENV.md)** - Environment variables and configuration
- **[Deployment Guide](docs/deployment/deployment-guide.md)** - Complete deployment procedures
- **[API Integration](docs/deployment/api-integration.md)** - External API setup and configuration
- **[Operations Guide](docs/production/operations-guide.md)** - Production operations and monitoring
- **[Troubleshooting](docs/production/troubleshooting.md)** - Issue resolution procedures
- **[Firebase Security](docs/production/firebase-security-rules.md)** - Firebase security configuration

### 🚀 Deployment and Operations
For maintainers and operators, comprehensive documentation is available:

- **Development Setup**: Start with [Environment Setup](README_ENV.md)
- **Production Deployment**: Follow [Deployment Guide](docs/deployment/deployment-guide.md)
- **Operational Support**: Reference [Operations Guide](docs/production/operations-guide.md)
- **Issue Resolution**: Use [Troubleshooting Guide](docs/production/troubleshooting.md)

## Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Wear OS App   │    │   Firebase      │    │  External APIs  │
│                 │────│   Firestore     │    │                 │
│ - Watch Face    │    │                 │    │ - OpenMeteo     │
│ - Mood Logging  │    │ - User Data     │    │ - NOAA SWPC     │
│ - Background    │    │ - Global Logs   │    │                 │
│   Workers       │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Components
- **Watch Face Service**: Displays real-time environmental data
- **Data Repository**: Manages API integration and local caching  
- **Background Workers**: Scheduled data collection and sync
- **Firebase Integration**: Secure cloud storage and authentication
- **Sensor Providers**: Location services and magnetometer readings

## External Services

### Weather Data
- **[OpenMeteo API](https://open-meteo.com/)**: Weather forecasts and current conditions
- **Free Tier**: 10,000 requests/day (no API key required)

### Space Weather Data  
- **[NOAA Space Weather Prediction Center](https://www.swpc.noaa.gov/)**: Solar activity and geomagnetic data
- **Public API**: No authentication required

### Backend Services
- **Firebase Firestore**: Data persistence and real-time sync
- **Firebase Authentication**: User authentication (anonymous)
- **Google Play Services**: Location services

## Development

### Build System
- **Gradle**: Build automation and dependency management
- **Kotlin**: Primary development language
- **Jetpack Compose**: UI framework for Wear OS

### Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires connected device)
./gradlew connectedAndroidTest

# Run with coverage
./gradlew testDebugUnitTestCoverage
```

### Code Quality
```bash
# Run lint checks
./gradlew lintDebug

# Generate signed release build
./gradlew assembleRelease
```

## Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** and add tests
4. **Run the test suite**: `./gradlew test`
5. **Commit your changes**: `git commit -m 'Add amazing feature'`
6. **Push to the branch**: `git push origin feature/amazing-feature`
7. **Open a Pull Request**

### Development Guidelines
- Follow existing code style and patterns
- Add tests for new functionality
- Update documentation for user-facing changes
- Ensure all CI checks pass

## Security

- **No API keys in code**: Use environment variables
- **Firebase security rules**: Restrict data access by user
- **Minimal permissions**: Request only necessary device permissions
- **Regular updates**: Keep dependencies current

## License

This project is licensed under the Apache License 2.0 - see the license headers in individual files for details.

## Support

- **Documentation**: See [docs/](docs/) directory for comprehensive guides
- **Issues**: Report bugs and feature requests via GitHub Issues
- **Discussions**: Use GitHub Discussions for questions and community support

## Acknowledgments

- **OpenMeteo**: Weather data API
- **NOAA SWPC**: Space weather data
- **JetBrains**: IDE and development tools
- **Android Team**: Wear OS platform and tools

---

**Last Updated**: 2025-09-08T13:50:00-04:00 / 2025-09-08T17:50:00Z — Initial repository documentation with comprehensive deployment and operations guides
