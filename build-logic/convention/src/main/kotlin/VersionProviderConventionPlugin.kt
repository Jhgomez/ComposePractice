import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

class VersionProviderConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java) {
            val applicationExtension = project.extensions
                .getByType(ApplicationAndroidComponentsExtension::class.java)

            val release = applicationExtension.selector().withBuildType("release")

            applicationExtension
                .onVariants(release) { variant ->
                    println("Current variant is ${variant.name}")

                    // Register single task for getting versions from git
                    val gitVersionProvider =
                        project.tasks.register(variant.name + "GitVersionProvider", GitVersionTask::class.java) {
                            gitVersionOutputFile.set(
                                File(project.layout.buildDirectory.get().asFile.absoluteFile, "intermediates/gitVersionProvider/release/output")
                            )
                            outputs.upToDateWhen { false } // never use cache
                        }

                    val manifestUpdater =
                        project.tasks.register(variant.name + "ManifestUpdater", ManifestTransformerTask::class.java) {
                            gitInfoFile.set(gitVersionProvider.flatMap(GitVersionTask::gitVersionOutputFile))
                        }
                    // update manifest with version information that we got from gitVersionProvider
                    variant.artifacts.use(manifestUpdater)
                        .wiredWithFiles(
                            ManifestTransformerTask::mergedManifest,
                            ManifestTransformerTask::updatedManifest
                        )
                        .toTransform(SingleArtifact.MERGED_MANIFEST)
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
        } + 1

        println("Number of git tags: $version")
        gitVersionOutputFile.get().asFile.writeText(version.toString())
    }
}

internal abstract class ManifestTransformerTask: DefaultTask() {

    @get:InputFile
    abstract val gitInfoFile: RegularFileProperty

    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    @get:OutputFile
    abstract val updatedManifest: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val gitVersion = gitInfoFile.get().asFile.readText()
        var manifest = mergedManifest.asFile.get().readText()
        manifest = manifest.replaceFirst(
            regex = "android:versionCode=\"[0-9]+\"".toRegex(),
            replacement = "android:versionCode=\"$gitVersion\""
        )
        println("Writes to " + updatedManifest.get().asFile.absolutePath)
        updatedManifest.get().asFile.writeText(manifest)
    }
}