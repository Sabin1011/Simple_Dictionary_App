plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "np.com.bimalkafle.easydictionary"
    compileSdk = 34

    defaultConfig {
        applicationId = "np.com.bimalkafle.easydictionary"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1") // Downgrade to stable version

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // Fix invalid version
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    implementation("androidx.core:core-ktx:1.12.0")  // Downgrade from 1.15.0 (unstable)
    implementation("androidx.appcompat:appcompat:1.6.1")  // Downgrade from 1.7.0 (unstable)
    implementation("com.google.android.material:material:1.10.0")  // Stable version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")  // Stable version
    implementation("androidx.activity:activity-ktx:1.8.0")  // Stable version

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")  // Downgrade from 2.11.0 (unstable)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
