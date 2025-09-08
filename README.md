# wAether - Wear OS Weather and Space Weather App

An advanced Wear OS application that provides comprehensive weather and space weather data, featuring atmospheric conditions, solar activity monitoring, and geomagnetic field measurements.

## 🏗️ Project Structure

This is an Android/Kotlin project built for Wear OS devices, featuring:
- **Weather Data**: Local weather conditions via OpenMeteo API
- **Space Weather**: Solar wind, X-ray flux, and KP index monitoring via NOAA SWPC
- **Sensor Integration**: Magnetometer readings for magnetic field strength
- **Data Persistence**: Firebase Firestore for cloud storage
- **Background Processing**: WorkManager for periodic data collection

## 🔒 Security & Dependency Management

This project implements comprehensive security practices and automated dependency management to ensure safe, up-to-date dependencies.

### 🛡️ Security Features

- **Dependency Locking**: All dependencies are locked to specific versions using Gradle dependency locking
- **Vulnerability Scanning**: Automated OWASP dependency checks on every build
- **Automated Updates**: Dependabot configured for weekly dependency updates
- **Security Monitoring**: GitHub dependency graph and security advisories
- **CI/CD Security**: Automated security checks in pull requests

### 📦 Dependency Management Workflow

#### 1. **Adding New Dependencies**

When adding new dependencies to the project:

```kotlin
// In app/build.gradle.kts
dependencies {
    implementation("com.example:new-library:1.0.0")
}
```

After adding dependencies, **always** update the lock files:

```bash
./gradlew dependencies --write-locks
```

Then commit both the build file changes and the generated lock files.

#### 2. **Updating Dependencies**

**Manual Updates:**
```bash
# Update a specific dependency version in build.gradle.kts
# Then regenerate lock files
./gradlew dependencies --write-locks

# Verify no vulnerabilities were introduced
./gradlew dependencyCheckAnalyze
```

**Automated Updates:**
- Dependabot automatically creates pull requests for dependency updates weekly
- Security updates are prioritized and can be auto-merged
- All updates are tested in CI before merging

#### 3. **Dependency Lock Files**

Lock files ensure reproducible builds by pinning exact dependency versions:

- **Location**: `gradle/dependency-locks/*.lockfile`
- **Purpose**: Ensures all developers and CI use identical dependency versions
- **Updates**: Only through explicit `--write-locks` commands
- **Security**: Prevents supply chain attacks through unexpected dependency changes

#### 4. **Vulnerability Scanning**

**OWASP Dependency Check:**
```bash
# Run vulnerability scan locally
./gradlew dependencyCheckAnalyze

# View report
open app/build/reports/dependency-check/dependency-check-report.html
```

**CI/CD Integration:**
- Automatic vulnerability scanning on every push and PR
- Daily security scans via scheduled workflows
- Build fails on high-severity vulnerabilities (CVSS >= 7.0)
- Security findings are automatically commented on PRs

#### 5. **Managing False Positives**

If a vulnerability is flagged incorrectly:

1. **Research the CVE** to confirm it's not applicable
2. **Add suppression** to `app/owasp-suppressions.xml`:

```xml
<suppress>
    <notes><![CDATA[
    CVE-2023-12345 affects server-side usage only. 
    Our Android app uses this library for client-side parsing only.
    ]]></notes>
    <packageUrl regex="true">^pkg:maven/com\.example/vulnerable-lib@.*$</packageUrl>
    <cve>CVE-2023-12345</cve>
</suppress>
```

3. **Document the reasoning** in the suppression file
4. **Commit the changes** and verify the build passes

### 🔄 Dependabot Configuration

Dependabot is configured to:
- **Schedule**: Weekly updates on Mondays at 6 AM UTC
- **Grouping**: Related dependencies are grouped (AndroidX, Kotlin, Firebase, etc.)
- **Limits**: Maximum 5 dependency PRs, 3 GitHub Actions PRs at a time
- **Auto-merge**: Security updates are candidates for automatic merging
- **Reviewers**: Automatically assigns repository maintainers

### 📋 Dependency Review Checklist

Before merging dependency updates:

- [ ] **CI passes**: All tests and security checks complete successfully
- [ ] **Breaking changes**: Review changelog for any breaking changes
- [ ] **Security impact**: No new high-severity vulnerabilities introduced
- [ ] **Compatibility**: Verify minimum API level and target SDK compatibility
- [ ] **Size impact**: Consider APK size impact for major version updates
- [ ] **Testing**: Smoke test critical app functionality if major updates

### 🚨 Security Incident Response

If a critical vulnerability is discovered:

1. **Assess Impact**: Determine if the vulnerability affects your usage
2. **Immediate Action**: If severe, consider temporarily removing the dependency
3. **Update Priority**: Expedite dependency update outside normal schedule
4. **Hotfix Process**: Use emergency release process for critical patches
5. **Communication**: Notify team and users if necessary

### 🛠️ Development Setup

#### Prerequisites

- **JDK 17** or higher
- **Android Studio** Hedgehog or newer
- **Gradle 8.11+** (managed by wrapper)

#### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Lint checks
./gradlew lint

# Security scan
./gradlew dependencyCheckAnalyze

# Update dependency locks
./gradlew dependencies --write-locks
```

#### Environment Variables

For local development, create a `local.properties` file:

```properties
# NVD API key for faster vulnerability database updates (optional)
nvd.api.key=your-nvd-api-key-here
```

### 📊 Monitoring & Reporting

- **GitHub Security Tab**: View dependency vulnerabilities and security advisories
- **Dependabot Alerts**: Automatic notifications for vulnerable dependencies
- **CI Reports**: Build artifacts include detailed security and dependency reports
- **Metrics**: Track dependency update frequency and security posture

### 🤝 Contributing

When contributing to this project:

1. **Never ignore security warnings** in CI
2. **Update lock files** when modifying dependencies
3. **Run local security scans** before submitting PRs
4. **Document dependency choices** in commit messages
5. **Follow semantic versioning** for dependency updates

### 📝 Change Log

- `Updated: 2024-12-08T13:30:00-05:00 / 2024-12-08T18:30:00Z — Added comprehensive dependency management and security documentation`

---

## 🧪 Architecture

- **MVVM Pattern**: ViewModels manage UI state and business logic
- **Repository Pattern**: DataRepository abstracts data sources
- **Dependency Injection**: Manual DI with default parameters (ready for Hilt migration)
- **Coroutines & Flow**: Reactive data handling and async operations
- **Jetpack Compose**: Modern UI toolkit for Wear OS

## 🚀 Features

- Real-time weather data integration
- Space weather monitoring (solar flares, geomagnetic activity)
- Device sensor integration (magnetometer)
- Cloud data synchronization
- Background data collection
- Wear OS optimized UI

## 📱 Installation

1. **Clone the repository**
2. **Open in Android Studio**
3. **Sync project with Gradle files**
4. **Configure Firebase** (add your `google-services.json`)
5. **Build and deploy** to Wear OS device or emulator

## 🤖 APIs Used

- **OpenMeteo**: Weather data and atmospheric conditions
- **NOAA SWPC**: Space weather data (solar wind, X-ray flux, KP index)
- **Firebase Firestore**: Cloud data storage and synchronization

## 📄 License

[Add your license information here]

## 🆘 Support

For dependency management questions or security concerns, please:
1. Check existing GitHub Issues
2. Review security advisories in the Security tab
3. Create a new issue with detailed description
4. For security vulnerabilities, use GitHub's private security reporting

---

**Last Updated**: December 8, 2024