plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "by.bsuir"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("io.kotest:kotest-runner-junit5:4.6.3")
    testImplementation("io.kotest:kotest-property:4.6.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}