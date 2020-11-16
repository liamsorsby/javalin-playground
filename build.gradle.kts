import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.owasp", "dependency-check-gradle", "3.1.2")
    }
}

plugins {
    kotlin("jvm") version "1.4.10"
    id("io.gitlab.arturbosch.detekt").version("1.15.0-RC1")
}
group = "com.sorsby.liam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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

    // Detekt plugins
    detektPlugins("io.gitlab.arturbosch.detekt", "detekt-formatting"," 1.15.0-RC1") // detekt wrapper around ktlint
}

detekt {
    toolVersion = "1.15.0-RC1"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}
