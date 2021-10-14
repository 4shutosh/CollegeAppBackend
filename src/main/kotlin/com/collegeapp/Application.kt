package com.collegeapp

import com.collegeapp.plugins.configureAuthentication
import com.collegeapp.plugins.configureRouting
import com.collegeapp.plugins.configureSecurity
import com.collegeapp.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        CollegeApplication().apply { module() }
    }.start(wait = true)
}

// this is for testing
fun Application.module() {
    CollegeApplication().apply { module() }
}

class CollegeApplication {

    fun Application.module() {
        configureRouting()
        configureSerialization()
        configureSecurity()
        configureAuthentication()
    }
}
