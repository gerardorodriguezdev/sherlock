import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.kmpCompose)
    alias(libs.plugins.kmpComposeCompiler)
    alias(libs.plugins.androidApplication)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidViewModel)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidActivity)
            implementation(libs.androidMLKit)

            project.dependencies {
                debugImplementation(compose.uiTooling)
                debugImplementation(libs.androidLeakCanary)
            }
        }
    }
}


android {
    namespace = "org.sherlock.sherlock"
    compileSdk = libs.versions.androidTargetSdk.get().toInt()

    defaultConfig {
        applicationId = "org.sherlock.sherlock"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
