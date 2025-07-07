plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kmpCompose) apply false
    alias(libs.plugins.kmpComposeCompiler) apply false
    alias(libs.plugins.kmp) apply false
}