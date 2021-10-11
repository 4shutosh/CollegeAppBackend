package com.collegeapp

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.collegeapp.plugins.configureRouting
import com.collegeapp.plugins.configureSecurity
import com.collegeapp.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
