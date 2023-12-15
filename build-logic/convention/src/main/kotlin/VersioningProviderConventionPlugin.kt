import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.konan.file.use
import java.io.File

class VersioningProviderConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java) {
            val applicationExtension = project.extensions
                .getByType(ApplicationAndroidComponentsExtension::class.java)

            with(applicationExtension) {
                val release = selector().withBuildType("debug")

                applicationExtension
                    .onVariants(release) { variant ->
                        println("Current variant is ${variant.name}")
                        // Because an app module can have multiple outputs when using multi-APK, versionCode
                        // is only available on the variant output.
                        // Gather the output when we are in single mode and there is no multi-APK.
                        val mainOutput = variant.outputs.single { it.outputType == OutputType.SINGLE }

                        // Register single task for getting versions from git
                        val gitTagListProvider =
                            project.tasks.register(variant.name + "GitTagListProvider", GitTagListTask::class.java) {
                                gitTagListOutputFile.set(
                                    File(project.layout.buildDirectory.get().asFile.absoluteFile, "intermediates/versioning/release/tagsOutput")
                                )
                                outputs.upToDateWhen { false } // never use cache
                            }

                        val gitVersionCodeProvider =
                            project.tasks.register(variant.name + "GitVersionCodeProvider", GitVersionCodeProvider::class.java) {
                                gitTagListInputFile.set(gitTagListProvider.flatMap(GitTagListTask::gitTagListOutputFile))
                                gitVersionCodeOutputFile.set(
                                    project.layout.buildDirectory.file("intermediates/versioning/release/versionCodeOutput")
                                )
                            }

                        mainOutput.versionCode.set(
                            gitVersionCodeProvider
                                .flatMap { task ->
                                    task.gitVersionCodeOutputFile.map { versionCodeOutput ->
                                        versionCodeOutput.asFile.readText().toInt()
                                    }
                                }
                        )

                        val gitVersionNameProvider =
                            project.tasks.register(variant.name + "GitVersionNameProvider", GitVersionNameProvider::class.java) {
                                gitTagListInputFile.set(gitTagListProvider.flatMap(GitTagListTask::gitTagListOutputFile))
                                gitVersionNameOutputFile.set(
                                    project.layout.buildDirectory.file("intermediates/versioning/release/versionNameOutput")
                                )
                            }

                        mainOutput.versionName.set(
                            gitVersionNameProvider
                                .flatMap { task ->
                                    task.gitVersionNameOutputFile.map { versionNameOutput ->
                                        versionNameOutput.asFile.readText()
                                    }
                                }
                        )
                    }
            }
        }

    }
}

internal abstract class GitTagListTask: DefaultTask() {

    @get:OutputFile
    abstract val gitTagListOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val process = ProcessBuilder(
            "/bin/zsh",  // shell address in file system
            "-c",
            "git show-ref --tags"
        ).start()

        val error = process.errorStream.bufferedReader().use { buffer ->
                buffer.readLine()
        }

        if (error?.isNotEmpty() != null) {
            System.err.println("Git error : $error")
        }

        val tags = process.inputStream.bufferedReader().use { buffer ->
            buffer.lines().toList()
        }

        gitTagListOutputFile.get().asFile.bufferedWriter().use { writer ->
            tags.forEach {
                writer.write(it)
                writer.appendLine()
            }
        }
    }
}

internal abstract class GitVersionNameProvider: DefaultTask() {

    @get:InputFile
    abstract val gitTagListInputFile: RegularFileProperty

    @get:OutputFile
    abstract val gitVersionNameOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val process = ProcessBuilder(
            "/bin/zsh",  // shell address in file system
            "-c",
            "git rev-parse HEAD" // will give the HEAD's commit hash so we can compare it against hashes in tags
        ).start()

        val error = process.errorStream.bufferedReader().use { buffer ->
            buffer.readLine()
        }

        if (error?.isNotEmpty() != null) {
            System.err.println("Git error : $error")
        }

        val headCommitHash = process.inputStream.bufferedReader().use { buffer ->
            buffer.readLine()
        }

        val versionName = gitTagListInputFile.get().asFile.bufferedReader().use { buffer ->
            buffer.lines().filter { tagAndCommit ->
                tagAndCommit.contains(headCommitHash)
            }.toList().firstOrNull()?.run {
                val versionStartIndex = lastIndexOf("/") + 1
                val name = substring(versionStartIndex)
                name
            }
        } ?: "0.0.1"

        gitVersionNameOutputFile.get().asFile.writeText(versionName)
    }
}

internal abstract class GitVersionCodeProvider: DefaultTask() {

    @get:InputFile
    abstract val gitTagListInputFile: RegularFileProperty

    @get:OutputFile
    abstract val gitVersionCodeOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val version = gitTagListInputFile.get().asFile.readLines().count()

        gitVersionCodeOutputFile.get().asFile.writeText(version.toString())
    }
}