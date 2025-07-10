plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.androidBenchmark)
    alias(libs.plugins.androidKotlin)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "org.benchmark"
    compileSdk = libs.versions.androidTargetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    testBuildType = "release"
    buildTypes {
        debug {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "benchmark-proguard-rules.pro")
        }
        release {
            isDefault = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    androidTestImplementation(project(":shared"))
    androidTestImplementation(libs.androidRunner)
    androidTestImplementation(libs.androidJunit)
    androidTestImplementation(libs.jvmJunit)
    androidTestImplementation(libs.androidBenchmark)
}