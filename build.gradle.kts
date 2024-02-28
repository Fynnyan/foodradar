import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile

plugins {
    id("com.bmuschko.docker-remote-api") version "6.7.0"
}

val g = "ch.menetekel"
val v = "1.2.1"

group = g
version = v

subprojects {
    group = g
    version = v
}

val beProject = project(":radar-backend")
val feProject = project(":radar-frontend")

tasks.register<Copy>("copyJarForBuild") {
    group = "docker"
    dependsOn(beProject.tasks["bootJar"])
    from(beProject.layout.buildDirectory.file("libs/${rootProject.name}-${version}.jar"))
    into(layout.buildDirectory.dir("docker"))
}

tasks.register<Copy>("copyFrontendForBuild") {
    group = "docker"
    dependsOn(feProject.tasks["npmBuild"])
    from(layout.projectDirectory.dir(feProject.projectDir.path + "/build"))
    into(layout.buildDirectory.dir("docker/static-files"))
}

val createDockerfile by tasks.creating(Dockerfile::class) {
    group = "docker"
    from("eclipse-temurin:17")
    copyFile("${rootProject.name}-${version}.jar", "/app/food-radar.jar")
    copyFile("static-files", "/app/static-files")
    entryPoint("java")
    defaultCommand("-jar", "/app/food-radar.jar")
    exposePort(8080)
}

val buildImage by tasks.creating(DockerBuildImage::class) {
    group = "docker"
    dependsOn(createDockerfile, "copyJarForBuild", "copyFrontendForBuild")
    images.add("food-radar:$version")
    inputDir = file("${project.buildDir}/docker")
    dockerFile = file("${project.buildDir}/docker/Dockerfile")
}

