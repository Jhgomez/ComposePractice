import com.android.build.api.dsl.ApplicationExtension
import okik.tech.community.admin.configureKotlinAndroid
import okik.tech.community.admin.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("okik.android.versioning")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk =
                    libs.findVersion("targetSdk").get().toString().toInt()
                configureKotlinAndroid(this)

                dependencies {
                    add("implementation", libs.findLibrary("androidx.core.splashscreen").get())
                }
            }
        }
    }
}
