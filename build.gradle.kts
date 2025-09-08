// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Apply the Android Application plugin to this project and its subprojects.
    id("com.android.application") apply false
    // Apply the Kotlin Android plugin to this project and its subprojects.
    id("org.jetbrains.kotlin.android") apply false
    // Google Services plugin for Firebase
    id("com.google.gms.google-services") apply false
    // KSP plugin for annotation processing (e.g., Room, Moshi)
    id("com.google.devtools.ksp") apply false
    // ktlint plugin for Kotlin linting
    id("org.jlleitschuh.gradle.ktlint") apply false
    // Dependency check plugin for security
    id("org.owasp.dependencycheck") version "8.4.3" apply false
    // Gradle versions plugin for dependency updates
    id("com.github.ben-manes.versions") version "0.50.0" apply false
}

// It's now common practice to define plugin versions in settings.gradle.kts's pluginManagement block
// or use version catalogs. If you do that, this block might be simpler or even empty
// if all plugins are applied in module-level build.gradle files.
// However, explicitly declaring them here ensures they are available project-wide.

// Task to clean the build directory.
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
