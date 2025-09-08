plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") // Google services plugin for Firebase
    id("com.google.devtools.ksp") version "1.9.23-1.0.19" // Or your current KSP version
    id("jacoco")
}

android {
    namespace = "com.wAether"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wAether"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // Ensure compatibility
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    // Core Kotlin & AndroidX
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // **** ADD/UPDATE FRAGMENT DEPENDENCY HERE ****
    // The lint error suggests version 1.3.0 or higher. Let's use a more recent stable one.
    implementation("androidx.fragment:fragment-ktx:1.7.0") // Or check for the latest stable version

    // Google Play Services for Wearables
    implementation("com.google.android.gms:play-services-wearable:18.2.0")

    // Jetpack Compose for Wear OS
    val wearComposeVersion = "1.3.1"
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-navigation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-tooling:$wearComposeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.7")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.7")

    // Wear OS Watch Face APIs
    val watchfaceVersion = "1.2.1"
    implementation("androidx.wear.watchface:watchface:$watchfaceVersion")
    implementation("androidx.wear.watchface:watchface-complications-data:$watchfaceVersion")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:$watchfaceVersion")
    implementation("androidx.wear.watchface:watchface-style:$watchfaceVersion")
    implementation("androidx.wear.watchface:watchface-editor:$watchfaceVersion")

    // Networking: Retrofit, OkHttp, Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    val coroutinesVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")

    // Lifecycle & ViewModel KTX
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    // implementation("com.google.firebase:firebase-auth-ktx")

    // Google Play Services Location
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("io.mockk:mockk-android:1.13.8")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.work:work-testing:2.9.0")
}

// JaCoCo Test Coverage Configuration
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest", "createDebugCoverageReport")
    
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/data/model/**/*.*",  // Exclude data models (mainly data classes)
        "**/ui/theme/**/*.*"     // Exclude theme/style files
    )
    
    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug")
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree.exclude(fileFilter)))
    executionData.setFrom(fileTree(project.buildDir).include(
        "jacoco/testDebugUnitTest.exec",
        "outputs/code_coverage/debugAndroidTest/connected/**/*.ec"
    ))
}

// Coverage verification task with minimum thresholds
tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    dependsOn("jacocoTestReport")
    
    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal() // 70% minimum coverage
            }
        }
        rule {
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO" 
                minimum = "0.60".toBigDecimal() // 60% branch coverage
            }
        }
    }
}
