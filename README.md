# wAether

A Wear OS watch face application for tracking weather and atmospheric conditions.

## Development Setup

### Quick Setup

Run the automated setup script:

```bash
./scripts/setup-dev.sh
```

This script will:
- Make gradlew executable
- Optionally install git pre-commit hooks
- Run initial code quality checks

### Manual Setup

#### Prerequisites

- Android Studio Arctic Fox or later
- JDK 17 or later
- Android SDK with Wear OS support
- Gradle 8.11.1+

### Code Quality & Linting

This project enforces code quality through automated linting and formatting tools:

#### Tools Used

- **ktlint**: Kotlin code formatting and style checking
- **detekt**: Static code analysis for Kotlin
- **EditorConfig**: Consistent coding style across editors

#### Local Development Commands

```bash
# Format Kotlin code automatically
./gradlew formatKotlin

# Check code style and run static analysis
./gradlew lintKotlin

# Run all code quality checks
./gradlew codeQuality

# Individual tool commands
./gradlew ktlintCheck      # Check formatting only
./gradlew ktlintFormat     # Auto-fix formatting issues
./gradlew detekt           # Run static analysis
```

#### CI/CD Integration

Code quality checks are automatically enforced in CI/CD:

- **Pre-merge**: All PRs must pass ktlint and detekt checks
- **Reports**: Detailed analysis reports are generated and uploaded as artifacts
- **Auto-formatting**: Use `./gradlew formatKotlin` before committing

#### Configuration Files

- `.editorconfig`: Editor-agnostic coding style configuration
- `config/detekt/detekt.yml`: Detekt rules and configuration
- Individual tool configurations in `app/build.gradle.kts`

#### IDE Integration

Most IDEs can integrate with these tools:

**Android Studio/IntelliJ IDEA:**
1. Install the ktlint plugin
2. Install the detekt plugin
3. Configure to use project's .editorconfig

**VS Code:**
1. Install Kotlin extension
2. Install EditorConfig extension

#### Bypassing Checks (Not Recommended)

If you need to bypass checks temporarily:

```bash
# Skip ktlint for specific files (add to .ktlintignore)
echo "path/to/file.kt" >> .ktlintignore

# Disable specific detekt rules (add to detekt.yml)
# See config/detekt/detekt.yml for examples
```

### Building the Project

```bash
# Build debug version
./gradlew assembleDebug

# Build release version
./gradlew assembleRelease

# Run unit tests
./gradlew testDebugUnitTest

# Run all checks and build
./gradlew check assembleDebug
```

### Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/wAether/     # Kotlin source files
│   │   └── res/                  # Android resources
│   └── test/                     # Unit tests
config/
└── detekt/
    └── detekt.yml               # Detekt configuration
.editorconfig                    # Code style configuration
.github/
└── workflows/
    └── code-quality.yml         # CI/CD pipeline
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run `./gradlew codeQuality` to ensure code quality
5. Commit your changes with conventional commit messages
6. Push to your branch and create a Pull Request

All contributions must pass the automated code quality checks before merging.

## License

[License information would go here]