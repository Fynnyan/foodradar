import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "7.0.0"
}

val npmStart by tasks.creating(NpmTask::class) {
    args = listOf("run", "start")
}

node {
    download = false
    version = "20.15.0"
}
