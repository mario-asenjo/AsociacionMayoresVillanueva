plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.masenjoandroid.asociacionmayoresvillanueva.app"
  compileSdk = 36

  buildFeatures {
    viewBinding = true
  }

  defaultConfig {
    applicationId = "com.masenjoandroid.asociacionmayoresvillanueva.app"
    minSdk = 26
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  // El bloque kotlinOptions ya no se usa así; se reemplaza con jvmToolchain
}

kotlin {
  // Configura Java 17 como jvmTarget
  jvmToolchain(17)
}

dependencies {
  // Splash
  implementation("androidx.core:core-splashscreen:1.0.1")

  // Core
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity.ktx)

  // Lifecycle
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)

  // UI
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.material)

  // Coroutines
  implementation(libs.kotlinx.coroutines.android)

  // Serialization
  implementation(libs.kotlinx.serialization.json)

  // Room
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  ksp(libs.androidx.room.compiler)

  // Tests
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.test.runner)
}
