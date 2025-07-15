plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.kmpCompose)
    alias(libs.plugins.kmpComposeCompiler)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kmpCoil)
            implementation(libs.kmpImmutable)
            implementation(libs.kmpViewModel)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidMLKit)
            project.dependencies {
                debugImplementation(compose.uiTooling)
            }
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
