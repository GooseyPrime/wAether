# Contributing to wAether

Thank you for your interest in contributing to wAether! This document provides guidelines and information for contributors.

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- Android SDK with Wear OS support
- Java 8 or higher
- Git
- Firebase account (for full testing)

### Setting Up Development Environment

1. **Fork and Clone**
   ```bash
   git clone https://github.com/your-username/wAether.git
   cd wAether
   ```

2. **Set Up Firebase**
   - Create a Firebase project
   - Add `google-services.json` to `app/` directory
   - Configure Firestore database

3. **Build and Test**
   ```bash
   chmod +x gradlew
   ./gradlew clean build
   ./gradlew test
   ```

## 📋 Development Guidelines

### Code Style

- **Language**: Kotlin following [official conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Architecture**: MVVM with Repository pattern
- **Naming**: Use descriptive names for variables, functions, and classes
- **Documentation**: Document public APIs and complex logic
- **Formatting**: Consistent indentation and formatting

### Commit Guidelines

- Use [Conventional Commits](https://www.conventionalcommits.org/) format
- Write clear, descriptive commit messages
- Keep commits atomic and focused
- Include issue numbers when applicable

Example commit messages:
```
feat: add space weather data visualization
fix: resolve memory leak in watch face service
docs: update API documentation
test: add unit tests for mood logging
```

### Branch Naming

- `feature/feature-name` - New features
- `fix/issue-description` - Bug fixes
- `docs/update-description` - Documentation updates
- `refactor/component-name` - Code refactoring

## 🧪 Testing

### Unit Tests

- Write tests for new functionality
- Maintain or improve test coverage
- Test edge cases and error conditions
- Use meaningful test names

```bash
# Run unit tests
./gradlew test

# Run with coverage
./gradlew jacocoTestReport
```

### Integration Tests

- Test API integrations
- Verify database operations
- Test sensor interactions

```bash
# Run instrumented tests
./gradlew connectedAndroidTest
```

## 📝 Pull Request Process

### Before Submitting

1. **Create an Issue**: For significant changes, create an issue first to discuss
2. **Branch from Main**: Create your feature branch from the latest main
3. **Write Tests**: Add or update tests for your changes
4. **Update Documentation**: Update README, code comments, or Wiki as needed
5. **Test Thoroughly**: Ensure all tests pass and the app works on Wear OS

### PR Requirements

- **Clear Title**: Descriptive title explaining the change
- **Detailed Description**: Explain what changed and why
- **Issue Reference**: Link to related issues
- **Screenshots**: Include screenshots for UI changes
- **Checklist**: Complete the PR template checklist

### Review Process

1. **Automated Checks**: Ensure all CI checks pass (when available)
2. **Code Review**: Address reviewer feedback promptly
3. **Testing**: Verify changes work on Wear OS devices
4. **Approval**: Wait for maintainer approval before merging

## 🐛 Reporting Issues

### Bug Reports

Include the following information:
- **Device Information**: Wear OS version, device model
- **App Version**: Version where the bug occurs
- **Steps to Reproduce**: Clear, step-by-step instructions
- **Expected Behavior**: What should have happened
- **Actual Behavior**: What actually happened
- **Logs**: Relevant error logs or screenshots
- **Environment**: Any relevant environmental factors

### Feature Requests

When requesting features:
- **Use Case**: Explain the problem you're trying to solve
- **Proposed Solution**: Describe your suggested approach
- **Alternatives**: Consider alternative solutions
- **Implementation**: If applicable, suggest implementation details

## 🏗️ Architecture Guidelines

### Project Structure

Follow the established package structure:
```
com.wAether/
├── data/           # Data layer (models, repositories, network)
├── ui/             # UI layer (activities, composables, viewmodels)
├── service/        # Watch face service
├── sensor/         # Device sensor integration
├── util/           # Utility classes
└── workers/        # Background workers
```

### Dependency Injection

- Currently using manual DI
- Consider Hilt migration for new features
- Keep dependencies loosely coupled
- Use interfaces for testability

### Data Flow

- **Repository Pattern**: Centralize data access
- **ViewModels**: Handle UI state and business logic
- **Coroutines**: For asynchronous operations
- **Flow**: For reactive data streams

## 🔧 Technical Considerations

### Performance

- **Memory Usage**: Be mindful of memory constraints on Wear OS
- **Battery Life**: Optimize background operations
- **Network Usage**: Implement proper caching and retry logic
- **UI Responsiveness**: Keep UI operations on main thread minimal

### Accessibility

- Use semantic descriptions for UI elements
- Support TalkBack and other accessibility services
- Consider users with motor disabilities
- Test with accessibility tools

### Wear OS Specific

- **Small Screen**: Design for small screen sizes
- **Touch Targets**: Ensure touch targets are appropriately sized
- **Gestures**: Support standard Wear OS gestures
- **Battery**: Minimize battery drain from sensors and network

## 📚 Resources

### Documentation

- [Android Developer Documentation](https://developer.android.com/)
- [Wear OS Development](https://developer.android.com/training/wearables)
- [Jetpack Compose for Wear OS](https://developer.android.com/training/wearables/compose)
- [Firebase Documentation](https://firebase.google.com/docs)

### Tools

- [Android Studio](https://developer.android.com/studio)
- [Wear OS Emulator](https://developer.android.com/training/wearables/get-started/creating)
- [Firebase Console](https://console.firebase.google.com/)

### Community

- [Android Developers Community](https://developer.android.com/community)
- [Kotlin Community](https://kotlinlang.org/community/)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/wear-os)

## ❓ Questions?

If you have questions about contributing:

1. Check existing [Issues](https://github.com/GooseyPrime/wAether/issues) and [Discussions](https://github.com/GooseyPrime/wAether/discussions)
2. Create a new Discussion for general questions
3. Open an Issue for specific bugs or feature requests
4. Contact maintainers through GitHub

Thank you for contributing to wAether! 🌟