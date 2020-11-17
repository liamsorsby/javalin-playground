import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("io.gitlab.arturbosch.detekt").version("1.15.0-RC1")
    id("org.owasp.dependencycheck").version("6.0.3")
    jacoco
}

group = "com.sorsby.liam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.4.10"))
    implementation(kotlin("reflect", "1.4.10"))

    // app
    implementation("io.javalin", "javalin", "3.10.0")

    // logging
    implementation("log4j", "log4j", "1.2.17")
    implementation("org.slf4j", "slf4j-log4j12", "1.7.30")
    implementation("net.logstash.log4j", "jsonevent-layout", "1.7")

    // openAPI
    implementation("io.javalin", "javalin-openapi", "3.9.1")
    implementation("io.swagger.core.v3", "swagger-core", "2.1.4")
    implementation("org.webjars", "swagger-ui", "3.25.0")
    implementation("org.webjars.npm", "redoc", "2.0.0-rc.2")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.10.1")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.10.1")
    implementation("io.github.classgraph", "classgraph", "4.8.34")

    // typesafe config
    implementation("com.typesafe","config","1.4.0")

    implementation("io.prometheus", "simpleclient_httpserver", "0.9.0")
    implementation("io.prometheus", "simpleclient_hotspot", "0.9.0")
    implementation("io.prometheus", "simpleclient_log4j", "0.9.0")
    implementation("com.mashape.unirest", "unirest-java", "1.4.9")

    // Testing
    testImplementation(kotlin("test-junit5"))

    // Spec testing frameworks
    testImplementation("org.spekframework.spek2", "spek-dsl-jvm", "2.0.4")
    testRuntimeOnly("org.spekframework.spek2", "spek-runner-junit5", "2.0.4")
    testImplementation("com.natpryce", "hamkrest", "1.6.0.0")
    testImplementation("khttp", "khttp", "0.1.0")
    testImplementation("io.mockk", "mockk", "1.10.0")

    // Detekt plugins
    runtimeOnly("io.gitlab.arturbosch.detekt", "detekt-cli", "1.15.0-RC1")
    detektPlugins("io.gitlab.arturbosch.detekt", "detekt-formatting", "1.15.0-RC1")
}

detekt {
    input = files("src/main/kotlin")
    buildUponDefaultConfig = true
    config = files(project.rootDir.resolve("config/detekt/detekt.yml"))
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
    environment("APPLICATION_PORT", "8080")
    environment("PROMETHEUS_PORT", "8081")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}
