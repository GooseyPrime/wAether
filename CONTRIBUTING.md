# Contributing to wAether

Thank you for your interest in contributing to wAether! This document provides guidelines and information for contributors.

## Development Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Git

### Local Setup
1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/wAether.git
   cd wAether
   ```
3. Open the project in Android Studio
4. Wait for Gradle sync to complete

## Development Workflow

### Before Making Changes
1. Create a new branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make sure all tests pass:
   ```bash
   ./gradlew test
   ```

### Code Quality Standards

#### Linting
Run linting before committing:
```bash
./gradlew ktlintCheck
./gradlew lintDebug
```

Fix linting issues:
```bash
./gradlew ktlintFormat
```

#### Testing
- Write unit tests for all new functionality
- Ensure all existing tests continue to pass
- Add instrumented tests for UI components when applicable

#### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add documentation for public APIs
- Keep functions small and focused

### Commit Guidelines
- Use clear, descriptive commit messages
- Follow the format: `type: description`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Example:
```
feat: add solar wind speed display to watch face
fix: resolve crash when location permission denied
docs: update API documentation for DataRepository
```

## Pull Request Process

### Before Submitting
1. Ensure all CI checks pass locally:
   ```bash
   ./gradlew test
   ./gradlew ktlintCheck
   ./gradlew lintDebug
   ```
2. Update documentation if needed
3. Add tests for new functionality

### PR Checklist
- [ ] Branch is up to date with `main`
- [ ] All tests pass
- [ ] Code follows style guidelines
- [ ] Documentation updated
- [ ] PR description explains changes clearly

### Review Process
1. Automated CI checks must pass
2. At least one maintainer review required
3. Address feedback promptly
4. Keep PR scope focused and small

## CI/CD Pipeline

Our automated pipeline includes:

### On Every PR/Push
- **Linting**: Kotlin and Android lint checks
- **Testing**: Unit and instrumented tests
- **Building**: Debug and release APKs
- **Security**: Vulnerability scanning

### Additional Checks
- **Weekly**: Dependency updates and security scans
- **On Release**: Automated deployment and distribution

## Project Structure

```
app/
├── src/
│   ├── main/java/com/wAether/
│   │   ├── ui/                 # UI components and screens
│   │   ├── data/              # Data layer (repositories, network)
│   │   ├── sensor/            # Hardware sensor interfaces
│   │   ├── util/              # Utility classes
│   │   └── workers/           # Background work managers
│   ├── test/                  # Unit tests
│   └── androidTest/          # Instrumented tests
├── build.gradle.kts          # App-level build configuration
└── proguard-rules.pro        # ProGuard configuration
```

## Areas for Contribution

We welcome contributions in these areas:

### High Priority
- Bug fixes and stability improvements
- Performance optimizations
- Test coverage improvements
- Documentation enhancements

### Features
- New data sources and APIs
- Additional watch face complications
- Enhanced user preferences
- Accessibility improvements

### Quality
- Code refactoring
- Design improvements
- Error handling enhancements
- Logging improvements

## Getting Help

- **Issues**: Check existing issues or create a new one
- **Discussions**: Use GitHub Discussions for questions
- **Documentation**: Refer to the README and code comments

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Help others learn and improve
- Focus on the project's goals

Thank you for contributing to wAether! 🌦️⌚
