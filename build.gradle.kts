// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Apply the Android Application plugin to this project and its subprojects.
    id("com.android.application") version "7.4.2" apply false // Stable AGP version
    // Apply the Kotlin Android plugin to this project and its subprojects.
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false // Match your Kotlin version
    // Google Services plugin for Firebase
    id("com.google.gms.google-services") version "4.4.1" apply false // Or latest stable
    // KSP plugin for annotation processing (e.g., Room, Moshi)
    id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false // Match your Kotlin & KSP version
    // OWASP Dependency Check for vulnerability scanning
    id("org.owasp.dependencycheck") version "9.2.0" apply false
    // Gradle Versions Plugin for dependency updates
    id("com.github.ben-manes.versions") version "0.51.0" apply false
}

// Enable dependency locking for all projects
allprojects {
    dependencyLocking {
        lockAllConfigurations()
        lockMode = LockMode.STRICT
    }
}

// It's now common practice to define plugin versions in settings.gradle.kts's pluginManagement block
// or use version catalogs. If you do that, this block might be simpler or even empty
// if all plugins are applied in module-level build.gradle files.
// However, explicitly declaring them here ensures they are available project-wide.

// Task to clean the build directory.
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
