import com.android.build.gradle.LibraryExtension
import okik.tech.community.admin.configureKotlinAndroid
import okik.tech.community.admin.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformCommonConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.native.cocoapods")
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("dev.icerock.mobile.multiplatform-resources")
            }

            plugins.withType(ComposePlugin::class.java) {
                extensions.configure<LibraryExtension> {
                    configureKotlinAndroid(this)

                    compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

                    testOptions.unitTests.isIncludeAndroidResources = true
                }

                extensions.configure<KotlinMultiplatformExtension> {
                    androidTarget() //this configuration could be removed, try removing it after all project is compiled
                    iosX64()
                    iosArm64()
                    iosSimulatorArm64()

                    sourceSets.commonMain {
                        dependencies {
                            implementation(libs.findLibrary("compose.foundation").get())
                            implementation(libs.findLibrary("compose.material3").get())
                            api(libs.findLibrary("icerock.moko.resources.compose").get())
                        }
                    }

                    sourceSets.androidMain {
                        dependsOn(sourceSets.getByName("commonMain"))
                        dependencies {
                            api(libs.findLibrary("activity.compose").get())
                        }
                    }

                    sourceSets.iosMain {
                        dependsOn(sourceSets.getByName("commonMain"))
                        sourceSets.getByName("iosX64Main").dependsOn(this)
                        sourceSets.getByName("iosArm64Main").dependsOn(this)
                        sourceSets.getByName("iosSimulatorArm64Main").dependsOn(this)
                    }

                    // Check if opt-in still needed in following Kotlin releases
                    // https://youtrack.jetbrains.com/issue/KT-61573
                    targets.configureEach {
                        compilations.configureEach {
                            compilerOptions.configure {
                                freeCompilerArgs.add("-Xexpect-actual-classes")
                            }
                        }
                    }

                    dependencies {
                        add("commonTestImplementation", libs.findLibrary("icerock.moko.resources.test").get())
                    }
                }
            }
        }
    }
}