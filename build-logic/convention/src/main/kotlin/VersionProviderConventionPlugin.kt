import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class VersionProviderConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.withType(AppPlugin::class.java) {
                extensions
                    .getByType(ApplicationAndroidComponentsExtension::class.java)
                    .onVariants { variant ->
                        val versionTask =
                            tasks.register(
                                variant.name + "VersionProvider",
                                GitVersionTask::class.java
                            ) {
                                val outPutFile = layout.buildDirectory.file("intermediates/gitVersionProvider/output")
                                gitVersionOutputFile.set(outPutFile)
                                //This will cause the task to run on every build, ideally we could
                                //add logic to pass inputs and this will execute an update when
                                //they have changed
                                this.outputs.upToDateWhen { false }
                            }

                        val version = versionTask
                            .get()
                            .gitVersionOutputFile
                            .get()
                            .asFile
                            .bufferedReader()
                            .use { content -> content.readLine().toInt()}

                        variant.outputs.forEach { output ->
                            output.versionCode.set(version)
                        }
                    }
            }
        }
    }
}

abstract class GitVersionTask: DefaultTask() {

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

        println(version)
        gitVersionOutputFile.get().asFile.writeText(version.toString())
    }
}