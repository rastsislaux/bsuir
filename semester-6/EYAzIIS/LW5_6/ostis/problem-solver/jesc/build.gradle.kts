plugins {
    kotlin("jvm") version "1.9.20"
    id("io.freefair.lombok") version "8.3"
    id("org.jetbrains.kotlin.plugin.lombok") version "1.8.0"
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "me.leepsky.cpu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.telegram:telegrambots:6.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.google.guava:guava:31.1-jre")

    implementation("io.ktor:ktor-server-netty:1.6.4")
    implementation("io.ktor:ktor-jackson:1.6.4")
    implementation("io.ktor:ktor-features:1.6.4")

    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

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