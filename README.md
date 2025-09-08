# wAether

[![CI](https://github.com/GooseyPrime/wAether/workflows/CI/badge.svg)](https://github.com/GooseyPrime/wAether/actions/workflows/ci.yml)
[![Release](https://github.com/GooseyPrime/wAether/workflows/Release/badge.svg)](https://github.com/GooseyPrime/wAether/actions/workflows/release.yml)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A weather and atmospheric data monitoring app for Wear OS devices.

## Features

- Real-time weather data integration
- Solar activity monitoring
- Magnetic field sensing
- Firebase data logging
- Custom watch face with atmospheric data
- Mood tracking with environmental correlation

## Development

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 17
- Android SDK API level 34
- Wear OS SDK

### Building

```bash
# Clone the repository
git clone https://github.com/GooseyPrime/wAether.git
cd wAether

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run linting
./gradlew ktlintCheck
```

### Testing

The project includes both unit tests and instrumented tests:

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run instrumented tests (requires emulator or device)
./gradlew connectedAndroidTest
```

### Code Quality

The project uses ktlint for Kotlin code formatting and Android Lint for static analysis:

```bash
# Check code formatting
./gradlew ktlintCheck

# Auto-format code
./gradlew ktlintFormat

# Run Android Lint
./gradlew lintDebug
```
## Getting Started

## CI/CD

The project uses GitHub Actions for continuous integration and deployment with comprehensive workflows:

### CI Pipeline (`ci.yml`)
Runs on every push and pull request to `main` and `develop` branches:

- **Lint Check**: 
  - Kotlin code formatting with ktlint
  - Android Lint static analysis
  - Uploads lint reports as artifacts

- **Unit Tests**: 
  - Executes all unit tests with JUnit
  - Generates test reports with detailed coverage
  - Publishes test results with pass/fail status

- **Instrumented Tests**: 
  - Runs on Android Wear OS emulator (API 30)
  - Tests app functionality on actual Android runtime
  - Caches emulator for faster subsequent runs

- **Build**: 
  - Assembles both debug and release APKs
  - Uploads APK artifacts for download
  - Generates build reports for debugging

### Security Pipeline (`security.yml`)
Runs on push/PR and weekly schedule:

- **Dependency Vulnerability Scan**: 
  - OWASP dependency check for known vulnerabilities
  - Scans all project dependencies for security issues
  - Generates detailed vulnerability reports

- **CodeQL Analysis**: 
  - GitHub's semantic code analysis
  - Detects security vulnerabilities and coding errors
  - Results appear in Security tab

### Release Pipeline (`release.yml`)
Triggered on version tags (e.g., `v1.0.0`):

- **Automated Testing**: Full test suite execution
- **APK/AAB Generation**: Signed release builds
- **GitHub Release**: Automatic release creation with artifacts
- **Play Store Ready**: Configured for Play Store deployment

### Artifacts Generated

All workflows generate artifacts for debugging and deployment:

- **Lint Reports**: HTML/XML reports for code quality issues
- **Test Reports**: JUnit XML and HTML test results
- **APK Files**: Debug and release Android packages
- **Security Reports**: Vulnerability assessments
- **Build Logs**: Detailed build information

### Status Badges

The badges at the top of this README show real-time status:
- 🟢 Green: All checks passing
- 🔴 Red: Build failures or test failures
- 🟡 Yellow: In progress

### Setting Up CI/CD

The workflows are pre-configured and will run automatically. For releases:

1. Update version in `app/build.gradle.kts`
2. Create and push a version tag: `git tag v1.0.0 && git push origin v1.0.0`
3. The release workflow builds and publishes automatically

For Play Store deployment, add these secrets to the repository:
- `SIGNING_KEY`: Base64-encoded keystore file
- `ALIAS`: Keystore alias name
- `KEY_STORE_PASSWORD`: Keystore password
- `KEY_PASSWORD`: Key password
- `SERVICE_ACCOUNT_JSON`: Play Store service account JSON

## Architecture

The app follows modern Android development practices:

- **MVVM Architecture** with ViewModel and Compose
- **Dependency Injection** ready (Hilt can be added)
- **Coroutines** for asynchronous operations
- **Retrofit** for network operations
- **Firebase Firestore** for data persistence
- **Wear OS Compose** for UI components

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
