plugins {
    //list of plugins used in the app
    alias(libs.plugins.kotlin.native.cocoapods) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.icerock.resource.generator) apply false
}
