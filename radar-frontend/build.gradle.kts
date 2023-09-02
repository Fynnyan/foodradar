import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "7.0.0"
}

val npmStart by tasks.creating(NpmTask::class) {
    args = listOf("run", "start")
}

val npmBuild by tasks.creating(NpmTask::class) {
    args = listOf("run", "build")
}

node {
    download = false
    version = "18.15.0"
}

tasks.register("clean", Delete::class.java) {
    delete = setOf(project.buildDir)
}