
import com.android.build.gradle.LibraryExtension
import okik.tech.community.admin.configureKotlinAndroid
import okik.tech.community.admin.libs
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getting
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

class MultiplatformCommonConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
            }

            plugins.withType(ComposePlugin::class.java) {
                extensions.configure<LibraryExtension> {
                    configureKotlinAndroid(this)
                }

                extensions.configure<KotlinMultiplatformExtension> {
                    androidTarget() //this configuration could be removed, try removing it after all project is compiled

                    listOf(
                        iosX64(),
                        iosArm64(),
                        iosSimulatorArm64()
                    ).forEach { iosTarget ->
                        iosTarget.binaries.framework {
                            baseName = "shared"
                            isStatic = true
                        }
                    }

                    sourceSets.commonMain {
                        dependencies {
                            implementation(libs.findLibrary("compose.foundation").get())
                            implementation(libs.findLibrary("compose.material3").get())
                        }
                    }

                    sourceSets.androidMain {
                        dependencies {
                            api(libs.findLibrary("activity.compose").get())
                        }
                    }
                }
            }


            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
            }
        }
    }
}