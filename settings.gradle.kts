// Defines the settings for the Gradle build.
// It's executed before the project's build.gradle files.

pluginManagement {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
        gradlePluginPortal() // Gradle Plugin Portal
    }
    plugins {
        id("com.android.application") version "8.1.4"
        id("org.jetbrains.kotlin.android") version "1.9.23"
        id("com.google.gms.google-services") version "4.4.1"
        id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Recommended for security and build reliability
    repositories {
        google()
        mavenCentral()
        // You can add other repositories here if needed, e.g., JitPack
        // maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "wAether" // Your project name
include(":app") // Include your app module

