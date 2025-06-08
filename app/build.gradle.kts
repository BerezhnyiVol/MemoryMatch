plugins {
    // Popis: Pluginy pre Android aplikáciu s podporou Jetpack Compose
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.memorymatch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.memorymatch"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Popis: Nastavenia pre release build (bez minifikácie kódu)
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
    // Popis: Základné Jetpack Compose knižnice
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Popis: Media3 knižnice pre prehrávanie videí a zvukov
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    // Popis: Room databáza (uloženie výsledkov hier)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)

    // Popis: LiveData, ViewModel a WorkManager komponenty
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.work.runtime.ktx)
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // Popis: Navigácia v Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Popis: Systémové UI ovládanie
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    // Popis: Podpora JUnit testovania
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Popis: Debug nástroje pre Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Popis: Prekrytie duplicitných deklarácií core knižníc
    implementation("androidx.core:core-ktx:1.10.1")
}
