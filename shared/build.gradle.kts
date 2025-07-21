plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kmpCoroutines)
        }
        androidMain.dependencies {
            implementation(libs.androidMLKit)
        }
    }
}

android {
    namespace = "org.sherlock"
    compileSdk = libs.versions.androidTargetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
    }
}
