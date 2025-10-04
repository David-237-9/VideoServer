plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "dev.david2379.videoserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.1")
    implementation("io.ktor:ktor-server-netty:2.3.1")
    implementation("org.slf4j:slf4j-simple:2.0.13")

    testImplementation(kotlin("test"))
}

application {
    mainClass = "dev.david2379.videoserver.MainKt"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
