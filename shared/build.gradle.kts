import dev.icerock.gradle.MRVisibility
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode

plugins {
    alias(libs.plugins.okik.multiplatform.shared)
}

private val PACKAGE_NAME = "okik.tech.community.admin.shared"

android {
    namespace = PACKAGE_NAME
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        podfile = project.file("../iosApp/Podfile")

        framework {
            export(libs.icerock.moko.resources.compose)
            export(libs.icerock.moko.resources)
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = PACKAGE_NAME
    multiplatformResourcesClassName = "SharedMR"
    multiplatformResourcesVisibility = MRVisibility.Public
}