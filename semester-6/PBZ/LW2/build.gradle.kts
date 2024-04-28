plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "bel.dadomu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.jena:apache-jena-libs:5.0.0")
    implementation("org.yaml:snakeyaml:2.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}