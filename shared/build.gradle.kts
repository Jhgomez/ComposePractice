plugins {
    alias(libs.plugins.okik.multiplatform.shared)
}

android {
    namespace = "okik.tech.community.admin.shared"
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
}