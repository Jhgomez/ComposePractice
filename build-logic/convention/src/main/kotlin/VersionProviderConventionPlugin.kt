import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

class VersionProviderConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java) {
            val applicationExtension = project.extensions
                .getByType(ApplicationAndroidComponentsExtension::class.java)

            with(applicationExtension) {
                val release = selector().withBuildType("release")

                applicationExtension
                    .onVariants(release) { variant ->
                        println("Current variant is ${variant.name}")
                        // Because an app module can have multiple outputs when using multi-APK, versionCode
                        // is only available on the variant output.
                        // Gather the output when we are in single mode and there is no multi-APK.
                        val mainOutput = variant.outputs.single { it.outputType == OutputType.SINGLE }

                        // Register single task for getting versions from git
                        val gitVersionProvider =
                            project.tasks.register(variant.name + "GitVersionProvider", GitVersionTask::class.java) {
                                gitVersionOutputFile.set(
                                    File(project.layout.buildDirectory.get().asFile.absoluteFile, "intermediates/gitVersionProvider/release/output")
                                )
                                outputs.upToDateWhen { false } // never use cache
                            }

                        mainOutput.versionCode.set(
                            gitVersionProvider
                                .flatMap{ task ->
                                    task.gitVersionOutputFile.map { versionOutput ->
                                        versionOutput.asFile.readText().toInt()
                                    }
                                }
                        )
                    }
            }
        }

    }
}

internal abstract class GitVersionTask: DefaultTask() {

    @get:OutputFile
    abstract val gitVersionOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val process = ProcessBuilder(
            "/bin/zsh",  // shell address in file system
            "-c",
            "git tag"
        ).start()

        val error = process.errorStream.use { stream ->
            stream.bufferedReader().use { buffer ->
                buffer.readLine()
            }
        }

        if (error?.isNotEmpty() != null) {
            System.err.println("Git error : $error")
        }

        val version = process.inputStream.use { stream ->
            stream.bufferedReader().use { buffer ->
                buffer.lines().count()
            }
        }

        println("Number of git tags: $version")
        gitVersionOutputFile.get().asFile.writeText(version.toString())
    }
}
