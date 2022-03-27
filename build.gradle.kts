val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kmongo_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
}

group = "collegeapp"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}


tasks.create("stage") {
    dependsOn("installDist")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    implementation("io.insert-koin:koin-ktor:3.1.5")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.0.0-beta-1")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:2.0.0-beta-1")

    //    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}