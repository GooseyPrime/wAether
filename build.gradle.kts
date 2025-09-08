// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.google.gms:google-services:4.3.15")
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

// It's now common practice to define plugin versions in settings.gradle.kts's pluginManagement block
// or use version catalogs. If you do that, this block might be simpler or even empty
// if all plugins are applied in module-level build.gradle files.
// However, explicitly declaring them here ensures they are available project-wide.

// Task to clean the build directory.
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
