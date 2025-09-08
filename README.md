# wAether

A Wear OS application for tracking weather and space weather data with mood correlation analysis.

## Overview

wAether is a Wear OS smartwatch application that combines traditional weather data with space weather information (solar activity, magnetic field data, etc.) to help users understand potential correlations with their mood and well-being.

## Features

- **Watch Face Integration**: Real-time weather and space weather display
- **Multi-Source Data Collection**: 
  - Local weather (OpenMeteo API)
  - Space weather data (NOAA SWPC)
  - Device sensors (magnetometer, location)
  - User mood tracking
- **Firebase Integration**: Secure data storage and analysis
- **Background Monitoring**: Periodic automated data collection

## Technology Stack

- **Platform**: Android Wear OS
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose for Wear OS
- **Architecture**: MVVM with Repository pattern
- **Data Sources**:
  - OpenMeteo API (weather data)
  - NOAA Space Weather Prediction Center API
  - Android sensor framework
- **Storage**: Firebase Firestore
- **Build System**: Gradle with Kotlin DSL

## Project Structure

```
wAether/
├── app/
│   ├── src/main/java/com/wAether/
│   │   ├── data/          # Data layer (models, network, repository)
│   │   ├── ui/            # UI layer (composables, viewmodels)
│   │   ├── sensor/        # Sensor data providers
│   │   ├── workers/       # Background tasks
│   │   └── util/          # Utility classes
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```
## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Wear OS SDK
- Kotlin 1.9.23+
- Android Gradle Plugin 8.10.0+

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/GooseyPrime/wAether.git
   cd wAether
   ```

2. Open the project in Android Studio

3. Set up Firebase:
   - Add your `google-services.json` file to the `app/` directory
   - Configure Firebase Firestore in your project

4. Build and run:
   ```bash
   ./gradlew build
   ```

### Testing on Wear OS

- Use a physical Wear OS device or
- Set up a Wear OS emulator in Android Studio

## Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details on:

- Issue templates and triage process
- Code style and quality standards
- Development workflow
- Testing requirements

### Quick Start for Contributors

1. Check our [Issues](https://github.com/GooseyPrime/wAether/issues) for `good-first-issue` labels
2. Read the [Contributing Guide](CONTRIBUTING.md)
3. Fork the repository and create a feature branch
4. Submit a pull request with your changes

## Issue Tracking

We use GitHub Issues with structured templates for:

- **Bug Reports**: Report crashes, incorrect behavior, or unexpected results
- **Feature Requests**: Suggest new functionality or enhancements
- **Documentation**: Improve or add to project documentation

Please use the appropriate issue template and provide complete information to help us address your concerns quickly.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Weather data provided by [Open-Meteo](https://open-meteo.com/)
- Space weather data from [NOAA Space Weather Prediction Center](https://www.swpc.noaa.gov/)
- Built with Android Jetpack and Wear OS technologies

## Support

- **Documentation**: Check this README and the [Contributing Guide](CONTRIBUTING.md)
- **Questions**: Use [GitHub Discussions](https://github.com/GooseyPrime/wAether/discussions)
- **Bug Reports**: Create an issue using our bug report template
- **Feature Requests**: Create an issue using our feature request template

---

*Made with ❤️ for the Wear OS community*
