# Trash-2-Cash
For code of Trash 2 Cash application

# Gradle(Module:App)
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.trash2cash"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.trash2cash"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation ("androidx.compose.ui:ui:1.6.7")
    implementation ("androidx.compose.material3:material3:1.6.7")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.7")
    implementation ("com.google.firebase:firebase-bom:32.6.0")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.activity:activity-compose:1.8.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

# Login
For code of Trash 2 Cash application

# Profile
For code of Trash 2 Cash application

# MainActivity.kt
For code of Trash 2 Cash application

# CameraX
For code of Trash 2 Cash application

# Firebase
For code of Trash 2 Cash application

# Cash
For code of Trash 2 Cash application

# Location Services
For code of Trash 2 Cash application

# Room
For code of Trash 2 Cash application


