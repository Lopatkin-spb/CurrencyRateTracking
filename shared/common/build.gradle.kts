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

    /**
     * DI: Dagger 2
     */
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    /**
     * Serialization: Json
     */
    implementation(libs.kotlinx.serialization.json)
}