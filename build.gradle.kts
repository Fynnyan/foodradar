import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Property

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.bmuschko.docker-remote-api") version "6.7.0"
    id("nu.studer.jooq") version "8.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "ch.menetekel"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.jsoup:jsoup:1.16.1")
    implementation("org.apache.pdfbox:pdfbox:3.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    jooqGenerator("org.jooq:jooq-meta-extensions:${jooq.version}")
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name =  "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties.apply {
                            // https://www.jooq.org/doc/latest/manual/code-generation/codegen-ddl/
                            add(Property().apply {
                                key = "scripts"
                                value = "src/main/resources/db/migration/V001__init.sql"
                            })
                            add(Property().apply {
                                key = "unqualifiedSchema"
                                value = "public"
                            })
                        }
                        forcedTypes = listOf(
                            ForcedType().apply {
                                userType = "kotlin.Array<java.time.DayOfWeek>"
                                converter = "ch.menetekel.foodradar.DayOfWeekConverter"
                                includeExpression = ".*\\.food_truck\\.days"
                            }
                        )

                    }
                    generate.apply {
                        isPojos = true
                        isRecords = true
                    }
                    target.apply {
                        packageName = "ch.menetekel.foodradar.jooq"
                        directory = "${project.buildDir}/generated/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    allInputsDeclared.set(true)
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Copy>("copyJarForBuild") {
    group = "docker"
    dependsOn(tasks.bootJar)
    from(layout.buildDirectory.file("libs/food-radar-$version.jar"))
    into(layout.buildDirectory.dir("docker"))
}

tasks.register<Copy>("copyFrontendForBuild") {
    group = "docker"
    dependsOn(":frontend:npmBuild")
    from(layout.projectDirectory.dir("frontend/build"))
    into(layout.buildDirectory.dir("docker/static-files"))
}

val createDockerfile by tasks.creating(Dockerfile::class) {
    group = "docker"
    from("eclipse-temurin:17")
    copyFile("food-radar-$version.jar", "/app/food-radar.jar")
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
