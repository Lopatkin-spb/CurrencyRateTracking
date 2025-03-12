plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.currencyratetracking.currencies"
    compileSdk = 34

    defaultConfig {
        minSdk = 30
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
        kotlinCompilerExtensionVersion = "1.5.2" // Corrected after change version of kotlin
    }
}

dependencies {
    implementation(project(":shared:common"))
    implementation(project(":shared:common-android"))
    implementation(project(":shared:core"))
    implementation(project(":shared:model"))
    implementation(project(":shared:api-locale"))
    implementation(project(":shared:api-remote"))
    implementation(project(":shared:ui-theme"))

    /**
     * Kotlin
     */
    implementation(libs.ktx.core)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    /**
     * DI: Dagger 2
     */
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    /**
     * Multithreading: Coroutines
     */
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    /**
     * Ui: Compose
     */
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.runtime:runtime-livedata")
    // Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Ui tests
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Integration
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") //Lifecycle
}