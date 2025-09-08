// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Apply the Android Application plugin to this project and its subprojects.
    id("com.android.application") version "8.1.4" apply false
    // Apply the Kotlin Android plugin to this project and its subprojects.
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    // Google Services plugin for Firebase
    id("com.google.gms.google-services") version "4.4.0" apply false
    // KSP plugin for annotation processing (e.g., Room, Moshi)
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    // ktlint for code formatting
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    // detekt for static code analysis
    id("io.gitlab.arturbosch.detekt") version "1.23.4" apply false
}

// It's now common practice to define plugin versions in settings.gradle.kts's pluginManagement block
// or use version catalogs. If you do that, this block might be simpler or even empty
// if all plugins are applied in module-level build.gradle files.
// However, explicitly declaring them here ensures they are available project-wide.

// Task to clean the build directory.
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Additional helpful tasks for code quality
tasks.register("formatKotlin") {
    group = "formatting"
    description = "Fix Kotlin code style deviations with ktlint"
    dependsOn(":app:ktlintFormat")
}

tasks.register("lintKotlin") {
    group = "verification"
    description = "Check Kotlin code style with ktlint and run detekt analysis"
    dependsOn(":app:ktlintCheck", ":app:detekt")
}

tasks.register("codeQuality") {
    group = "verification"
    description = "Run all code quality checks (ktlint + detekt)"
    dependsOn("lintKotlin")
}
