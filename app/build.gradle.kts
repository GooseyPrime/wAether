plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") // Google services plugin for Firebase
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" // Or your current KSP version
    id("org.jlleitschuh.gradle.ktlint") // ktlint for code formatting
    id("io.gitlab.arturbosch.detekt") // detekt for static code analysis
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
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

// ktlint configuration
ktlint {
    version.set("1.0.1")
    debug.set(false)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
    
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

// detekt configuration
detekt {
    toolVersion = "1.23.4"
    config.setFrom(file("../config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
    
    source.setFrom(
        "src/main/java",
        "src/test/java",
        "src/androidTest/java"
    )
    
    parallel = true
    
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}
