# Contributing to wAether

Thank you for your interest in contributing to wAether! This document outlines the process for contributing to the project and explains our issue triage and labeling system.

## Issue Tracking and Triage Process

### Creating Issues

We use GitHub issue templates to ensure consistent and complete issue reports. When creating an issue, please:

1. **Choose the appropriate template**:
   - **Bug Report**: For reporting bugs or unexpected behavior
   - **Feature Request**: For suggesting new features or enhancements
   - **Documentation**: For documentation issues or improvements

2. **Fill out all required fields** in the template completely and accurately

3. **Use clear, descriptive titles** that start with the appropriate prefix:
   - `[BUG]` for bug reports
   - `[FEATURE]` for feature requests  
   - `[DOCS]` for documentation issues

### Issue Triage Process

Our issue triage process follows these steps:

#### 1. Initial Triage (within 48 hours)
- New issues are automatically labeled with `needs-triage`
- Maintainers review the issue for completeness and validity
- Issues are labeled with appropriate **type**, **component**, and **platform** labels
- Priority is assessed and **priority** labels are applied
- Status is updated to `needs-investigation` or `ready-for-work`

#### 2. Investigation Phase
Issues labeled `needs-investigation` require:
- Technical feasibility assessment
- Impact analysis
- Resource requirement estimation
- Dependencies identification

#### 3. Ready for Work
Issues labeled `ready-for-work` have:
- Clear acceptance criteria
- Defined scope
- No blocking dependencies
- Assigned priority level

#### 4. In Progress
- Issues being actively worked on are labeled `in-progress`
- Assignee should provide regular updates
- Issues blocking progress are labeled `blocked`

## Label System

### Label Categories

#### Type Labels (Required)
- `bug` - Something isn't working correctly
- `enhancement` - New feature or improvement request
- `documentation` - Documentation related issues
- `question` - Requests for clarification or help

#### Priority Labels (Required after triage)
- `priority/critical` - Security issues, app crashes, data loss
- `priority/high` - Major functionality broken, significant UX issues
- `priority/medium` - Minor bugs, nice-to-have features
- `priority/low` - Cosmetic issues, far-future enhancements

#### Component Labels (Recommended)
- `component/watch-face` - Watch face display and rendering
- `component/sensors` - Sensor data collection (magnetometer, location, etc.)
- `component/data` - Data processing, APIs, Firebase integration
- `component/ui` - User interface and user experience
- `component/build` - Build system, dependencies, CI/CD

#### Platform Labels
- `platform/wear-os` - Wear OS specific issues
- `platform/android` - General Android platform issues

#### Status Labels (Managed by maintainers)
- `needs-triage` - New issue requiring triage
- `needs-investigation` - Requires technical investigation
- `ready-for-work` - Ready for development
- `in-progress` - Actively being worked on
- `blocked` - Blocked by dependencies

#### Special Labels
- `good-first-issue` - Suitable for new contributors
- `help-wanted` - Seeking community contributions
- `duplicate` - Duplicate of existing issue
- `wontfix` - Will not be addressed
- `invalid` - Invalid or incomplete issue

### Label Application Guidelines

#### For Issue Reporters
- Do not manually apply labels (except during template selection)
- Provide complete information to help with proper labeling
- Update issues with new information as needed

#### For Maintainers
1. **Always apply** at least one type label and one priority label
2. **Add component labels** to help with assignment and filtering
3. **Update status labels** as work progresses
4. **Use platform labels** when issues are specific to Wear OS or Android
5. **Apply special labels** to encourage community participation

## Issue Assignment Process

### Self-Assignment
- Contributors can self-assign issues labeled `ready-for-work`
- Comment on the issue before starting work
- Limit concurrent assignments based on issue complexity

### Maintainer Assignment
- Critical and high-priority issues may be directly assigned
- Core maintainers handle `component/build` and security issues
- New contributors are guided to `good-first-issue` items

## Communication Guidelines

### Issue Comments
- Keep comments focused and constructive
- Provide regular updates on assigned issues
- Ask questions if requirements are unclear
- Use @mentions for specific attention

### Issue Lifecycle
1. **Open** → `needs-triage`
2. **Triaged** → `needs-investigation` or `ready-for-work`
3. **Investigated** → `ready-for-work` or `blocked`
4. **Assigned** → `in-progress`
5. **Completed** → **Closed** with reference to fixing PR

## Quality Standards

### Issue Quality
- Complete template information
- Clear reproduction steps (for bugs)
- Specific acceptance criteria (for features)
- Relevant context and screenshots

### Code Quality
- Follow existing code style and patterns
- Include appropriate tests
- Update documentation as needed
- Ensure CI/CD passes

## Getting Help

- **Questions**: Use [GitHub Discussions](https://github.com/GooseyPrime/wAether/discussions)
- **Security Issues**: Report privately via [Security Advisories](https://github.com/GooseyPrime/wAether/security/advisories/new)
- **Documentation**: Check existing docs and contribute improvements

## Recognition

Contributors are recognized through:
- Attribution in release notes
- Contributor acknowledgments
- Issue and PR history

Thank you for helping make wAether better for everyone!
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
