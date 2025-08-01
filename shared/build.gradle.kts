plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.nativeCocoapods)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "2.0"
        summary = "Kotlin Multiplatform Google MLKit text recognition implementation"
        homepage = "https://github.com/gerardorodriguezdev/sherlock"
        ios.deploymentTarget = "16.0"

        pod("GoogleMLKit/Vision") {
            moduleName = "MLKitVision"
            version = "9.0.0"
        }

        pod("GoogleMLKit/TextRecognitionCommon") {
            moduleName = "MLKitTextRecognitionCommon"
            version = "9.0.0"
        }

        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "shared"
            isStatic = true
        }
    }

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
