plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.currencyratetracking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.currencyratetracking"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_URL", "\"https://api.vatcomply.com/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true //code shrinking
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
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
        kotlinCompilerExtensionVersion = "1.5.2" // Corrected after change version of kotlin
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    /** Modules */
    implementation(project(":shared:common-android"))
    implementation(project(":shared:core"))
    implementation(project(":shared:api-remote"))
    implementation(project(":shared:model"))
    implementation(project(":shared:common"))
    implementation(project(":shared:api-locale"))
    implementation(project(":feature:currencies"))
    implementation(project(":shared:ui-theme"))

    /**
     * Kotlin
     */
    implementation(libs.ktx.core)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    /**
     * Ui: Compose
     */
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    androidTestImplementation(composeBom)
    // Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // Ui tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Integration
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") //Lifecycle
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.navigation:navigation-compose:2.7.7") //Navigation must update after next stable release (fix empty args)

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
     * Tests
     */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}