import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    /** Modules */
    implementation(project(":shared:model"))
    implementation(project(":shared:common"))

    /**
     * DI: Dagger 2
     */
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    /**
     * Network
     */
    // Provider
    implementation(libs.retrofit)
    // Converter between provider and kotlin serialization
    implementation(libs.retrofit.converter.kotlinx.serialization)
    // For logging
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.logging.interceptor)

    /**
     * Serialization: Json
     */
    implementation(libs.kotlinx.serialization.json)
}