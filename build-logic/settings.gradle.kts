rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/ca-libs.versions.toml"))
        }
    }
}

include(":convention")

project(":convention").projectDir = File(rootDir, "convention")
